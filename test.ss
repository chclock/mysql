(load "mysql/mysql.sls")


(define conn (mysql:connect "localhost" "root" "123456" "sys"))

(mysql:query conn "SELECT * FROM sys.sys_config")