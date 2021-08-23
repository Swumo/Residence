# Residence
 
Multiple area protection plugin for a survival server!

### Features

- Ability to create multiple areas!

- Ability to change settings per area!

- Each Resident has their own permissions!

- Ability to change Guest Rules: allow non-residents to modify your residence!

- Creeper explosion and Enderman griefing protection!

- Customizable greeting/farewell messages when entering a residence!

- Ability to change the name of your residence!

- Get notified whenever someone enters your residence!

- And much more!

### Commands

```
[] - optional argument, <> - required argument

/res - Display the help message

/res menu - Open the residence menu

/res wand [player] - Get/give the residence claim wand (Admin only)

/res create - Create your residence

/res add <residence name> <player> - Add <player> to your residence

/res remove <residence name> <player> - Remove <player> from your residence

/res delete <residence name> - Delete your residence

/res delall - Delete ALL residences (Admin only)

/res list - List all residences (Admin only)

/res forceload - Force load every residence (Admin only) (Should only be used in EXTREME circumstances)

/res set <residence name> <greeting/farewell> <message | none | reset> - Set the greeting/farewell message to <message | none | default>

/res sethome - Set your residence home

/res home - Teleport to your residence home

/res oset <player> <blocks> - Set the max claim area for <player> in <blocks> (Admin Only)

/res maxarea - Check your maximum amount of claimable blocks

/res setname <new name> - Set a new name for your residence

/res showarea <start/stop> <residence name> - Start showing/stop showing the claimed area of your specified residence to other players with particles
```

### Dependencies

This plugin requires [LuckPerms](https://luckperms.net/download) in order to function!

### API

Everything in this plugin is completely accessible and using it is really simple! Below is an example how to create a Residence.
```java 
Location loc1 = player.getLocation();
Location loc2 = player.getLocation().add(loc1);
Cuboid area = new Cuboid(loc1, loc2);
Residence residence = new Residence(area, player.getName());
```

### Versions

This plugin supports Spigot [1.17.x](https://www.spigotmc.org/wiki/buildtools/#1-17-1)

### To-do

Open for suggestions!