class DevelopmentConfig(BaseConfig):
   """Development configuration options."""
   DEBUG = True
   ENV_NAME = 'psychen-melodium-dev'
   DOMAIN = 'https://xxx'
   DBUSER = 'gituser'
   DBPASSWORD = 'gituser1'
   MYSQL_LOCAL_URL = 'mysql://localhost:3306/mugunga?useSSL=false'