db.createUser({
  user: "admin",
  pwd: "password",
  roles: [
    { role: "userAdminAnyDatabase", db: "admin" },
    { role: "readWriteAnyDatabase", db: "admin" }
  ]
});

db = db.getSiblingDB("jotddb");
db.createUser({
  user: "jotduser",
  pwd: "jotdpass",
  roles: [
    { role: "readWrite", db: "jotddb" },
    { role: "dbAdmin", db: "jotddb" }
  ]
});
