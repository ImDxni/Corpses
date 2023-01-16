# Corpses Plugin for Minecraft

This plugin creates a fake npc when a player dies that rapresents his corpse, it has a despawn time,a lock time, and a ttl (Time to live) in the database.

**CITIZENS REQUIRED**

## Configuration File

```
database:
  log-ttl: 7 #database ttl in days
corpse:
  locked: 10 #how many seconds the corpse is locked (locked means that only the killer of the player can open his inventory)
  despawn: 100 #after how many seconds the body must despawn

inventory:
  corpselog:
    chest: "&7Log di %player% (%date%)" #Name of the item in the logs
  itemlog:
    lore: #lore of the item in the logs including placeholders
      - "&8• &7Raccolto da: &f%target%" 
      - "&8• &7Data: &f%date%"

messages: #messages
  corpse-locked: "&aNon puoi ancora aprire questo corpo! Attendi"
  no-logs: "&aNon ci sono pagine disponibili"
  
database: #database credentials ( MONGODB  )
  host: "hostname" #hostname
  port: 27017 #port (usually 27017 for mongo)
  database: "database" #name of the database
  authentication:
    enabled: true #if auth is enabled
    user: "user"
    password: "password"

```


## Screenshots

![2023-01-16_12 28 02](https://user-images.githubusercontent.com/39953274/212672550-715edd5c-c4c9-42f0-a648-98a284be342a.png)

![2023-01-16_12 30 59](https://user-images.githubusercontent.com/39953274/212672588-29161ccd-bf39-4a2f-b527-ef015eb1b794.png)

![2023-01-16_12 31 03](https://user-images.githubusercontent.com/39953274/212672599-833bc5b5-9d84-4ca6-9561-19ecad0bd2de.png)

