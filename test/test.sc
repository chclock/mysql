(import (mysql mysql))

(define conn (mysql:connect "localhost" "root" "123456" "sys"))

(display (mysql:query conn "SELECT * FROM sys.sys_config"))
