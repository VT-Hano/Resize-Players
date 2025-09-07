# Resize Players ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2FOnys-0%2FResize-Players%2Fmain%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'version'%5D&label=version)
Resize yourself or other players to your desired height! Allows reach to be affected as well.

## Features
- **Player Scaling**: Change your own size, another player's size, or everyone online using a simple command.
- **Reach Adjustment**: Player reach distance can be configured to change based on their size, using linear, exponential, or logarithmic formulas.
- **PlaceholderAPI Support**: Display player size and reach information in other plugins.
- **Magic Armor**: A new, fully configurable item that resizes the wearer.
    - **Custom Item**: Define the item's ID, name, and lore in `armor.yml`.
    - **Configurable Effect**: Set a percentage-based shrink (or growth) effect that applies when the item is worn.
    - **Seamless Integration**: The `/resize` command and the magic armor now work together. Manually setting a new size with `/resize` while wearing the armor will update your "base" size, and the armor's effect will re-apply to this new size. When you unequip the armor, you will return to your last set base size.

## Commands
| Command | Description |
|---|---|
| `/resizeplayers help` | View the help menu. |
| `/resizeplayers reload` | Reload all configuration files (`config.yml`, `armor.yml`). |
| `/resizeplayers info [player]` | View the height, block reach, and entity reach of yourself or another player. |
| `/resize <blocks> [player]` | Resize yourself or another player to the specified amount of blocks. |
| `/resize <blocks> all` | Resize all players to the specified amount of blocks. |
| `/getresizeitem` | Gives the player the special resizing armor piece defined in `armor.yml`. |

## Permissions
| Permission | Description |
|---|---|
| `resizeplayers.scale.self` | Allows the player to scale themselves with `/resize <blocks>`. |
| `resizeplayers.scale.others` | Allows the player to scale other players with `/resize <blocks> <player>`. |
| `resizeplayers.scale.all` | Allows the player to scale all players with `/resize <blocks> all`. |
| `resizeplayers.scale.exempt` | Exempts the player from being scaled by others. |
| `resizeplayers.scale.bypass` | Allows the player to bypass the minimum and maximum scale limit. |
| `resizeplayers.reload` | Allows the player to reload the plugin with `/resizeplayers reload`. |
| `resizeplayers.info.self` | Allows the player to view their own size and reach with `/resizeplayers info`. |
| `resizeplayers.info.others` | Allows the player to view another player's size and reach with `/resizeplayers info <player>`. |
| `resizeplayers.getitem` | Allows the player to receive the special resizing armor with `/getresizeitem`. |


## [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) Placeholders
| Placeholder | Description |
|---|---|
| `%resizeplayers_height%` | The player's height in blocks. |
| `%resizeplayers_block-reach%` | The player's block reach in blocks. |
| `%resizeplayers_entity-reach%` | The player's entity reach in blocks. |
