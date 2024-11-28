# Fryc's Parry
Adds parry mechanics and allows blocking with items other than shield


## [CurseForge](https://www.curseforge.com/minecraft/mc-mods/sword-parry)
## [Modrinth](https://modrinth.com/mod/sword-parry)

------------------------------------------------------------------------------------------------------------

# Datapacks:

### Modifying pool of items that can parry:

- to add item to the pool: `data/frycparry/tags/item/items_can_parry.json`
- to exclude item from the pool (excluding has priority over including): `data/frycparry/tags/item/parrying_excluded_items.json`

(remember that some directories changed with 1.21, for example `items` is now `item`)

### Making mobs resistant to disarmed status effect:

- `data/frycparry/tags/entity_type/disarm_resistant_mobs.json`

### Modifying parry attributes:

#### Creating parry attributes:

If [existing parry attributes](https://github.com/Xires87/SwordParry/tree/master/src/main/resources/data/frycparry/parry_attributes) don't satisfy you, in `data/frycparry/parry_attributes/` create json file containing:
```json
{
  "parryTicks": 0,
  "meleeDamageTakenAfterBlock": 0.0,
  "projectileDamageTakenAfterBlock": 0.0,
  "explosionDamageTakenAfterBlock": 0.0,
  "cooldownAfterParryAction": 0.0,
  "cooldownAfterInterruptingBlockAction": 0.0,
  "cooldownAfterAttack": 0.0,
  "maxUseTime": 0,
  "blockDelay": 0,
  "explosionBlockDelay": 0,
  "shouldStopUsingItemAfterBlockOrParry": true,
  "knockbackAfterParryAction": 0.0,
  "parryEffects": [
    {
      "statusEffect": "mod_id:status_effect",
      "duration": 0,
      "amplifier": 1,
      "chance": 0.0,
      "enchantmentMultiplier": 0.0
    }
  ]
}
```
Change the values to meet your needs. Name of your file will be used to access them.


#### Applying parry attributes to selected items:

In `data/frycparry/parry_items/` create json file containing:
```json
{
  "parryAttributes": "NAME_OF_PARRY_ATTRIBUTES_FILE_WITHOUT_EXTENSION",
  "items": [
    "some_mod_id:some_item",
    "some_mod_id:another_item"
  ]
}
```
`parryAttributes` is name of file from `data/frycparry/parry_attributes/` WITHOUT `.json`

Inside `items` add items for which selected parry attributes will be applied

Example:
```json
{
  "parryAttributes": "my_parry_attributes",
  "items": [
    "minecraft:diamond_sword",
    "minecraft:diamond_pickaxe",
    "minecraft:stick",
    "minecraft:dirt"
  ]
}
```

Note:
- modifying parry attributes for items that can't parry by default (dirt and stick in example above) doesn't enable parrying for these items (you need to enable them with tag)


