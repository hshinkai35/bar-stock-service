# bar-stock-service

## データベース作成
- MySQLセットアップ
- init.sqlを実行
- application.ymlにてusername、password設定
```application.yml
spring:
  # MySQL接続情報
  datasource:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/bar?useSSL=false
    username: root
    password: root
```