# Residence
 
Multiple area protection plugin for a survival server!

### Features

Ability to create multiple areas!
Ability to change settings per area!
Each Resident has their own permissions!
Ability to change Guest Rules: allow non-residents to modify your residence!
Creeper explosion and Enderman griefing protection!
Customizable greeting/farewell messages when entering a residence!
Ability to change the name of your residence!
Get notified whenever someone enters your residence!
And much more!

### API

Everything in this plugin is completely accessible and using it is really simple! Below is an example how to create a Residence.
```java Location loc1 = player.getLocation();
Location loc2 = player.getLocation().add(loc1);
Cuboid area = new Cuboid(loc1, loc2);
Residence residence = new Residence(area, player.getName());
```

### Versions

This plugin supports Spigot [1.17.x](https://www.spigotmc.org/wiki/buildtools/#1-17-1)

### Todo

Open for suggestions!