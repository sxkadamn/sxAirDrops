# 🌟 sxAirDrops  

**sxAirDrops** is a powerful Minecraft plugin that introduces dynamic airdrops filled with valuable loot, making your server’s gameplay more exciting and unpredictable! 🪂💎  

## 🚀 Features  

- **Dynamic Airdrops** – Random loot chests drop from the sky at different locations.  
- **Fully Customizable** – Configure loot, drop frequency, and spawn locations.  
- **Player Notifications** – Alerts players when an airdrop appears to encourage competition.  
- **Multi-Version Support** – Compatible with multiple Minecraft versions and Spigot/Paper servers.  

## 👥 Installation  

1. Download the latest version from [Releases](https://github.com/sxkadamn/sxAirDrops/releases).  
2. Move `sxAirDrops.jar` to your server's `plugins` folder.  
3. Restart your server.  
4. Configure the plugin in `plugins/sxAirDrops/config.yml` as needed.  

## 🎮 Commands  

| Command                      | Description                                   |  
|------------------------------|-----------------------------------------------|  
| `/airdrop loot`           | Only for admin command which opening menu editor.       |  
## ⚙️ Configuration (`config.yml`)  

Example basic configuration:  

```yaml
event:
  task:
    time_mode: mythical #schedule, moscow, mythical
    moscowTime:
      time_one: '14:40'
      time_two: '15:20'
    settings:
      pre_time_message: 15
      time_to_start: 10
      time_to_destroy: 20
      time_to_open: 20
  animations:
    locked:
      anima: CREATIVE #CREATIVE, SPIRAL другие анимации
    opened:
      anima: SPIRAL #CREATIVE, ROD другие анимации
  bossbar:
    opened:
      color: RED
      style: SOLID
      name: "&6До удаления сундука осталось &e{time} &6секунд &a/ &6Координаты: &e{x} {y} {z}"
    locked:
      color: YELLOW
      style: SOLID
      name: "&6До открытия сундука осталось &e{time} &6секунд &a/ &6Координаты: &e{x} {y} {z}"
  holograms:
    locked: "&6Сундук закрыт и откроется через: &e{time} &6секунд"
    opened: "&6Сундук открыт и удалится через: &e{time} &6секунд"
  sounds:
    locked:
      - BLOCK_CHEST_LOCKED:3.5F:3.5F
    opened:
      - MUSIC_CREDITS:3.5f:3.5F
  tools:
    location:
      generate:
        world: "world"
        black_List_Biomes:
          - DEEP_OCEAN
          - OCEAN
          - RIVER
          - SWAMP
        black_List_Blocks:
          - LAVA
          - WATER
          - FIRE
          - LEAVES
          - CACTUS
          - ICE
          - SNOW
    block: #если схема выключена, то лучше ставить везде 0
      setting: #это для схематики, чтобы подстроить блок под схематику
        x: 0.0 #1 = по оси Х +1 блок
        y: 1.0 #1 = в высоту +1 блок
        z: 0.0 #1 = по оси Z +1 блок
    schematic:
      file: drop.schem
      enable: false
    region:
      enable: true
      size: 20
      flags:
        - pistons: deny
        - pvp: allow
        - build: deny
        - mob-spawning: allow
    attempts: 100
    serializate:
      x-direct-max: 2000
      x-direct-min: -2000
      z-direct-max: 2000
      z-direct-min: -2000
  chests:
    chest1:
      world: "world"
      x: 100
      y: 64
      z: 200
      key: "mythical_key_1"
    chest2:
      world: "world"
      x: 150
      y: 64
      z: 200
      key: "mythical_key_2"
    chest3:
      world: "world"
      x: 200
      y: 64
      z: 200
      key: "mythical_key_3"
    chest4:
      world: "world"
      x: 250
      y: 64
      z: 200
      key: "mythical_key_4"
  keys:
    key_1:
      id: "mythical_key_1"
      name: "Ключ 1"
      material: "TRIPWIRE_HOOK"
    key_2:
      id: "mythical_key_2"
      name: "Ключ 2"
      material: "TRIPWIRE_HOOK"
    key_3:
      id: "mythical_key_3"
      name: "Ключ 3"
      material: "TRIPWIRE_HOOK"
    key_4:
      id: "mythical_key_4"
      name: "Ключ 4"
      material: "TRIPWIRE_HOOK"
messages:
  must_be_chest: "Блок не является сундуком"
  rarity_not_found: "Выбранная редкость не существует."
  animation_not_found: "Анимация не найдена: {animation}"
  invalid_sound_configuration: "Неверная конфигурация звука: {soundData}"
  invalid_sound_format: "Неверный формат звука: {soundData}"
  plugin_not_enabled: "Плагин {pluginName} не работает."
  invalid_location_or_file: "Неверное местоположение или файл для вставки схемы."
  error_pasting_schematic: "Ошибка при вставке схемы: {error}"
  directory_created: "Каталог создан: {path}"
  directory_creation_failed: "Не удалось создать каталог: {path}"
  unsupported_schematic_format: "Неподдерживаемый формат схемы: {filename}"
  error_reading_schematic: "Ошибка при чтении схемы файла: {filename}"
  unsuitable_biome: "Неподходящий биом: {biome}"
  unsuitable_block: "Неподходящий блок на земле: {block}"
  location_not_found: "Не удалось найти подходящее место после {attempts} попыток."
  location_search_error: "Ошибка при поиске местоположения: {error}"
  failed_to_save_file: "Не удалось сохранить файл: {fileName}. Ошибка: {error}"
  failed_to_load_item: "Не удалось загрузить предмет: {key}"
  invalid_item: "Предмет {key} является некорректным или отсутствует, пропускаем."
  region_created: "&6Регион &e{name} &6успешно создан!"
  region_removed: "&6Регион &e{name} &6удален!"
  region_error: "&cОшибка при создании региона."
  location_failure: "Неудалось сгенерировать локацию: {error}"
  incorrect_stack: "Пропущен некорректный элемент: {stack}"
  invalid_chest_location: "Некорректная локация сундука: {chestLocation}"
  schematic_in_air: "Схема может быть размещена в воздухе. Проверьте расчеты высоты."
  block_is_not_chest: "Блок на локации {chestLocation} не является сундуком!"
  drop_spawned:
    - '&6Аирдроп был успешно заспавлен &a%x% &e%y% &a%z%'
    - '&6В нем: %rare%'
  pre_drop_spawned:
    - '&6Аирдроп появится примерно через: &e%time% &6секунд'
  commands:
    user-help:
      - "&7Помощь:"
      - "&6/airdrop loot - &7Открыть редактор лута"
      - "&7Используйте эту команду для настройки лута."
    loot_menu_opened: "&6Меню лута успешно открыто!"
    command_error: "Эту команду может выполнить только игрок"


menus:
  loot: "&6Редактирование лута сундука"
  loot_edit: "&6Сундук: &a&l{rarity}"
```

*For advanced configuration options, check the [documentation](https://github.com/sxkadamn/sxAirDrops/wiki).*  

## 📌 Requirements  

- Minecraft 1.16+  
- Spigot/Paper server  
- Java 8+  

## 🛠️ Contributing  

Want to improve sxAirDrops? Fork the repository, make your changes, and submit a Pull Request!  

## ❓ Support  

Have questions or suggestions? Open an issue in the [repository](https://github.com/sxkadamn/sxAirDrops/issues)!  

---

🔹 **Developer:** sxkadamn  

💥 Enhance your Minecraft server with exciting airdrops today! 🔥

