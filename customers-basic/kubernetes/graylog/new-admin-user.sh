curl -u admin:admin -X POST "http://localhost:9000/api/users" \
-H "Content-Type: application/json" \
-H "X-Requested-By: curl" \
-d '{
  "username": "graylog_admin",
  "password": "admin",
  "email": "admin@example.com",
  "timezone": "Asia/Kolkata",
  "roles": ["Admin"],
  "permissions": [],
  "first_name": "Graylog",
  "last_name": "Admin"
}'