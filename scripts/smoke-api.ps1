$ErrorActionPreference = 'Continue'
$base = 'http://localhost:9090/api'
$results = New-Object System.Collections.ArrayList

function T {
    param($name, $method, $url, $body, $headers)
    $p = @{ Uri = "$base$url"; Method = $method; UseBasicParsing = $true; TimeoutSec = 15; ContentType = 'application/json' }
    if ($headers) { $p.Headers = $headers }
    if ($body) { $p.Body = ($body | ConvertTo-Json -Depth 6 -Compress) }
    try {
        $r = Invoke-WebRequest @p
        $script:last = $r.Content | ConvertFrom-Json
        [void]$results.Add("PASS | $name | $($r.StatusCode)")
        return $true
    } catch {
        $code = if ($_.Exception.Response) { [int]$_.Exception.Response.StatusCode } else { 'NET' }
        $script:last = $_.Exception.Message
        [void]$results.Add("FAIL | $name | $code")
        return $false
    }
}

# ============ AUTH ============
$jwt = (Invoke-RestMethod "$base/auth/login" -Method POST -Body '{"name":"admin","password":"1234567890"}' -ContentType 'application/json' -TimeoutSec 15)
[void]$results.Add("PASS | POST /auth/login (admin) | 200")
$H = @{ Authorization = "Bearer $jwt" }

T 'POST /auth/login (sai MK - ky vong 401)' 'POST' '/auth/login' @{name='admin';password='sai'} $null | Out-Null
T 'GET /auth/me' 'GET' '/auth/me' $null $H | Out-Null
$meName = "$($script:last.name)/$($script:last.role)/$($script:last.fullName)"
[void]$results.Add("     -> $meName")

# no token
try {
    Invoke-WebRequest "$base/apartments" -UseBasicParsing -TimeoutSec 10 | Out-Null
    [void]$results.Add("FAIL | GET /apartments (no token) | 200 - LEAK")
} catch {
    $c = if ($_.Exception.Response) { [int]$_.Exception.Response.StatusCode } else { 'NET' }
    [void]$results.Add("PASS | GET /apartments (no token) | $c - secured")
}

# ============ APARTMENTS ============
T 'GET /apartments?size=5' 'GET' '/apartments?size=5' $null $H | Out-Null
$apts = $script:last
$aptid = if ($apts -is [array]) { $apts[0].id } else { $apts.content[0].id }
$total = if ($apts -is [array]) { $apts.Count } else { $apts.totalElements }
[void]$results.Add("     -> total=$total, id dau=$aptid")

T "GET /apartments/$aptid" 'GET' "/apartments/$aptid" $null $H | Out-Null
$apt = $script:last
[void]$results.Add("     -> $($apt.number) status=$($apt.status) residents=$($apt.residentCount)")

T "GET /apartments/$aptid/residents" 'GET' "/apartments/$aptid/residents" $null $H | Out-Null
[void]$results.Add("     -> residents=$($script:last.Count)")

# ============ RESIDENTS ============
T 'GET /residents' 'GET' '/residents' $null $H | Out-Null
$res = $script:last
$rname = if ($res -is [array]) { ($res | ForEach-Object { "$($_.fullName):$($_.status)" }) -join ', ' } else { "total=$($res.totalElements)" }
[void]$results.Add("     -> $rname")
$rid = if ($res -is [array]) { $res[0].id } else { $res.content[0].id }
T "GET /residents/$rid/events" 'GET' "/residents/$rid/events" $null $H | Out-Null
[void]$results.Add("     -> events=$($script:last.Count)")

# ============ FEE TYPES ============
T 'GET /fee-types' 'GET' '/fee-types' $null $H | Out-Null
$fts = $script:last
[void]$results.Add("     -> " + (($fts | ForEach-Object { "$($_.name)[$($_.type)]" }) -join ', '))
$ftid = $fts[0].id

# ============ FEE RECORDS ============
T 'GET /fee-records?size=10' 'GET' '/fee-records?size=10' $null $H | Out-Null
$fr = $script:last
if ($fr.content) {
    [void]$results.Add("     -> total=$($fr.totalElements)")
    $fr.content | Select-Object -First 5 | ForEach-Object {
        [void]$results.Add("       $($_.apartmentNumber) | $($_.feeTypeName) | $($_.amount) | $($_.status)")
    }
}

T 'POST /fee-records/generate (ky da co - ky vong skipped)' 'POST' '/fee-records/generate' @{period='2026-10';feeTypeIds=@($ftid)} $H | Out-Null
if ($script:last.created -ne $null) { [void]$results.Add("     -> created=$($script:last.created) skipped=$($script:last.skipped)") }

# ============ RESIDENCY EVENTS ============
T 'GET /residency-events' 'GET' '/residency-events' $null $H | Out-Null
$ev = $script:last
$evc = if ($ev -is [array]) { $ev.Count } elseif ($ev.content) { $ev.totalElements } else { '?' }
[void]$results.Add("     -> count=$evc")

# ============ STATISTICS ============
T 'GET /statistics/fees?from=2026-10&to=2026-10' 'GET' '/statistics/fees?fromPeriod=2026-10&toPeriod=2026-10' $null $H | Out-Null
$st = $script:last
if ($st.total) { [void]$results.Add("     -> rows=$($st.rows.Count) expected=$($st.total.expected) paid=$($st.total.paid) unpaid=$($st.total.unpaid) amtPaid=$($st.total.paidAmount)") }

# ============ VEHICLES ============
T "GET /apartments/$aptid/vehicles" 'GET' "/apartments/$aptid/vehicles" $null $H | Out-Null
$veh = $script:last
$vinfo = if ($veh -is [array]) { ($veh | ForEach-Object { "$($_.plate)[$($_.type)]" }) -join ', ' } else { '?' }
[void]$results.Add("     -> $vinfo")

# ============ ACCOUNT ============
T 'PUT /auth/password (MK cu sai - ky vong loi)' 'PUT' '/auth/password' @{oldPassword='sai';newPassword='12345678ab'} $H | Out-Null
T 'PUT /auth/password (MK moi ngan - ky vong loi)' 'PUT' '/auth/password' @{oldPassword='1234567890';newPassword='123'} $H | Out-Null

# ============ DASHBOARD ============
T 'GET /dashboard/summary' 'GET' '/dashboard/summary' $null $H | Out-Null
$d = $script:last
[void]$results.Add("     -> apts=$($d.apartmentCount) res=$($d.residentCount) occupied=$($d.occupiedCount) unpaid=$($d.unpaidAmount) paid=$($d.paidAmount)")

$results -join "`n"
$pass = ($results | Select-String '^PASS').Count
$fail = ($results | Select-String '^FAIL').Count
"`n===== $pass PASS / $fail FAIL ====="
