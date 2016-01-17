# power-kit
Minecraft mod based on Forge Mod Loader which allows administrators to define item kits

## Commands

`/pkit info` - displayes nbt tag of item which you're currently holding, if nbt tag is empty you wount see anything

`/pkit <kit name>` - gives the player the specified kit. Prints error if kit has been already used or not found

Note: user should have `powerkit.<kit>` permission in order to use kit, for example, if user wants to get `starter` kit, he should have permission `powerkit.starter`

## Configuration
In order to configure Power Kit you should create text file into `/config/` directory and name it `kit.json`

As a root you should create `kits` field with arrays of available kits.

#### Kit structure

`name` - title of a kit, will be used in game in commands

`interval` - interval between kit uses in seconds 

`items` - array of items available in kit

#### Item structure

`item` - unique identifier of item, it should look this `<modid>:<item name>`, for example `"minecraft:stone"` or `"IC2:itemIngot"`

`metadata` - metadata of item

`amount` - size of stack which would be given

`enchanments` - enchantments of item, it is a map with enchantment id as a key and it's level as value. This field is optional

`nbt` - nbt tag of item. This field is optional

#### Sample config:
```
{
  "kits": [{
    "name": "starter",
    "interval": 1000,
    "items": [{
      "item": "minecraft:stone",
      "metadata": 0,
      "amount": 32,
      "enchantments": {
        "1": 1
      },
      "nbt": {
        "for": [1, 10, 22],
        "bar": {
          "key": "value"
        }
      }
    }]
  }]
}
```
