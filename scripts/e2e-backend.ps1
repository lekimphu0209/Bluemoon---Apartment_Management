$ErrorActionPreference = 'Continue'
$base = 'http://localhost:9090'
$jwt = (Get-Content .jwt -Raw).Trim()
$H = @{ Authorization = "Bearer $jwt" }
$results = New-Object System.Collections.ArrayList

function Invoke-T {
    param($name, $method, $url, $body)
    $p = @{ Uri = "$base$url"; Method = $method; Headers = $H; UseBasicParsing = $true; TimeoutSec = 15; ContentType = 'application/json' }
    if ($body) { $p.Body = ($body | ConvertTo-Json -Depth 6 -Compress) }
    try {
        $r = Invoke-WebRequest @p
        $script:last = $r.Content | ConvertFrom-Json
        [void]$results.Add("PASS $name => $($r.StatusCode)")
        return $true
    } catch {
        $code = if ($_.Exception.Response) { [int]$_.Exception.Response.StatusCode } else { 'NET' }
        $msg = ''
        try { $sr = New-Object IO.StreamReader($_.Exception.Response.GetResponseStream()); $msg = $sr.ReadToEnd() } catch {}
        if ($msg.Length -gt 250) { $msg = $msg.Substring(0, 250) }
        $script:last = $msg
        [void]$results.Add("FAIL $name => $code $msg")
        return $false
    }
}

$aptid = [long](Get-Content .aptid -Raw).Trim()
$ftid  = [long](Get-Content .ftid -Raw).Trim()
$ftid2 = 2  # Phi dich vu T2 created earlier

# --- Resident ---
if (Invoke-T 'resident-create-HEAD' POST /api/residents @{fullName='Nguyen Van A';idCard='001234567890';apartmentId=$aptid;relationship='HEAD';phone='0901234567'}) {
    $script:rid = $script:last.id
    [void]$results.Add("   -> id=$rid status=$($script:last.status) apt=$($script:last.apartmentNumber)")
    Set-Content .rid -Value $rid -NoNewline
}
Invoke-T 'resident-dup-head(E3)' POST /api/residents @{fullName='Tran Thi B';idCard='001234567891';apartmentId=$aptid;relationship='HEAD'} | Out-Null
Invoke-T 'resident-dup-cccd(E1)' POST /api/residents @{fullName='Tran C';idCard='001234567890';apartmentId=$aptid;relationship='CHILD'} | Out-Null
Invoke-T 'apt-residents' GET "/api/apartments/$aptid/residents" $null | Out-Null
if (Invoke-T 'apt-detail' GET "/api/apartments/$aptid" $null) {
    [void]$results.Add("   -> status=$($script:last.status) residents=$($script:last.residentCount) head=$($script:last.headName)")
}

# --- Fee generate / collect ---
if (Invoke-T 'generate-2026-10' POST /api/fee-records/generate @{period='2026-10';feeTypeIds=@($ftid,$ftid2)}) {
    [void]$results.Add("   -> created=$($script:last.created) skipped=$($script:last.skipped)")
}
Invoke-T 'generate-repeat(A1)' POST /api/fee-records/generate @{period='2026-10';feeTypeIds=@($ftid)} | Out-Null
if ($script:last -and $script:last.created -ne $null) { [void]$results.Add("   -> re-gen created=$($script:last.created) skipped=$($script:last.skipped)") }
Invoke-T 'generate-voluntary(E3)' POST /api/fee-records/generate @{period='2026-10';feeTypeIds=@(9999)} | Out-Null

if (Invoke-T 'fee-records-list' GET "/api/fee-records?period=2026-10&size=5" $null) {
    [void]$results.Add("   -> total=$($script:last.totalElements)")
    $script:frid = $script:last.content[0].id
    [void]$results.Add("   -> first=$($script:last.content[0].apartmentNumber)/$($script:last.content[0].feeTypeName)/$($script:last.content[0].amount) $($script:last.content[0].status)")
}
if ($script:frid) {
    Set-Content .frid -Value $frid -NoNewline
    Invoke-T "collect-fr$frid" POST "/api/fee-records/$frid/collect" @{paidDate='2026-10-18'} | Out-Null
    if ($script:last.status) { [void]$results.Add("   -> after collect: $($script:last.status) paidDate=$($script:last.paidDate) by=$($script:last.collectedByName)") }
    Invoke-T "collect-again(409)" POST "/api/fee-records/$frid/collect" @{paidDate='2026-10-18'} | Out-Null
    Invoke-T "uncollect" POST "/api/fee-records/$frid/uncollect" $null | Out-Null
    if ($script:last.status) { [void]$results.Add("   -> after uncollect: $($script:last.status)") }
    Invoke-T "collect-final" POST "/api/fee-records/$frid/collect" @{paidDate='2026-10-18';note='test'} | Out-Null
}

# --- Contribution ---
if (Invoke-T 'create-donation' POST /api/fee-types @{name='Ung ho Tet';type='DONATION';startDate='2026-01-01';endDate='2026-12-31'}) {
    $script:donid = $script:last.id
    [void]$results.Add("   -> donation id=$donid mandatory=$($script:last.mandatory) calc=$($script:last.calcMethod)")
    Invoke-T 'contribution' POST /api/fee-records/contributions @{apartmentId=$aptid;feeTypeId=$donid;amount=500000;paidDate='2026-10-18'} | Out-Null
    if ($script:last.status) { [void]$results.Add("   -> contribution status=$($script:last.status) contrib=$($script:last.contribution)") }
    $script:confrid = $script:last.id
    Invoke-T 'uncollect-contrib(E2)' POST "/api/fee-records/$confrid/uncollect" $null | Out-Null
}

# --- Residency events ---
$rid2 = [long](Get-Content .rid -Raw).Trim()
if (Invoke-T 'event-temp-absence' POST /api/residency-events @{type='TEMP_ABSENCE';residentId=$rid2;eventDate='2026-10-10';fromDate='2026-10-10';toDate='2026-10-20'}) {
    [void]$results.Add("   -> event id=$($script:last.id)")
}
Invoke-T 'event-tempres-not-living' POST /api/residency-events @{type='TEMP_RESIDENCE';residentId=$rid2;eventDate='2026-10-15';fromDate='2026-10-15';toDate='2026-10-30'} | Out-Null
if (Invoke-T 'resident-after-absence' GET "/api/residents/$rid2" $null) {
    [void]$results.Add("   -> status=$($script:last.status)")
}
Invoke-T 'resident-events' GET "/api/residents/$rid2/events" $null | Out-Null
if ($script:last -is [array]) { [void]$results.Add("   -> events=$($script:last.Count)") }

# --- Statistics + dashboard ---
if (Invoke-T 'statistics' GET "/api/statistics/fees?fromPeriod=2026-10&toPeriod=2026-10" $null) {
    $t = $script:last.total
    [void]$results.Add("   -> rows=$($script:last.rows.Count) expected=$($t.expected) paid=$($t.paid) unpaid=$($t.unpaid) paidAmt=$($t.paidAmount)")
}
if (Invoke-T 'dashboard-final' GET /api/dashboard/summary $null) {
    $b = $script:last
    [void]$results.Add("   -> apts=$($b.apartmentCount) res=$($b.residentCount) unpaid=$($b.unpaidAmount) paid=$($b.paidAmount)")
}

# --- Vehicle (v2.0) ---
Invoke-T 'vehicle-create' POST "/api/apartments/$aptid/vehicles" @{type='MOTORBIKE';plate='29A-123.45'} | Out-Null
Invoke-T 'vehicle-dup-plate(E1)' POST "/api/apartments/$aptid/vehicles" @{type='CAR';plate='29A-123.45'} | Out-Null
Invoke-T 'vehicle-list' GET "/api/apartments/$aptid/vehicles" $null | Out-Null
if ($script:last -is [array]) { [void]$results.Add("   -> vehicles=$($script:last.Count)") }

# --- Account ---
Invoke-T 'update-me' PUT /api/auth/me @{fullName='Admin Updated'} | Out-Null
if ($script:last.fullName) { [void]$results.Add("   -> fullName=$($script:last.fullName)") }
Invoke-T 'pwd-wrong-old(E1)' PUT /api/auth/password @{oldPassword='wrong';newPassword='newpassword123'} | Out-Null
Invoke-T 'pwd-short(E2)' PUT /api/auth/password @{oldPassword='1234567890';newPassword='short'} | Out-Null

# --- No-token check ---
try {
    Invoke-WebRequest -Uri "$base/api/apartments" -UseBasicParsing -TimeoutSec 10 | Out-Null
    [void]$results.Add("FAIL no-token => 200 (should be 403)")
} catch {
    $c = if ($_.Exception.Response) { [int]$_.Exception.Response.StatusCode } else { 'NET' }
    [void]$results.Add("PASS no-token => $c (secured)")
}

Set-Content -Path tr2.txt -Value ($results -join "`n") -Encoding UTF8
"done: $($results.Count) lines"
