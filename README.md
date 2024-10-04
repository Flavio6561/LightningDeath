## Lightning Death,
a simple cliend side mod that spawns a lightning bolt on a player death position

### Configs:

- **`LightningCount`** How many bolts should a player spawn when dead
> ![Bolts](https://i.imgur.com/pyEYVzn.gif)  
> `value = 0 will disable the mod`

- **`IgnoreSelf`** Whether to include yourself as a bolt spawning souce
> ![OwnDeath](https://i.imgur.com/f1L9JnG.gif)  
> `On the right, the player does not summon a bolt`

### Configs:
- **ModMenu integration**:
> - ModMenu is supported
> - Commands are reserved to non-ModMenu users only
> - Mod settings will be saved in your config folder and updated at runtime.

- **Commands**:
> - `/ld SetLightningCount <value (0,20)>`
> - `/ld ignoreSelf <bool (true or false)>`

### Other:
> ![DoLog](https://cdn.modrinth.com/data/cached_images/c46a670261ed4cbf243e17ccd78034514d6ab4ad.png)  
> `In your latest.log there is the info about the last player death`

> The mod is entirely client side, this will make the mod work on servers, but will not affect server iteractions

[Any issue with the mod?](https://github.com/Flavio6561/LightningDeath/issues)
