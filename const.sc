(library (mysql const)
  (export
    mysql-stmt-init-done
    mysql-stmt-prepare-done
    mysql-stmt-execute-done
    mysql-stmt-fetch-done
    )
  (import (scheme))  

  (define mysql-stmt-init-done 1)
  (define mysql-stmt-prepare-done 2)
  (define mysql-stmt-execute-done 3)
  (define mysql-stmt-fetch-done 4)
)     