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
  myth_drop_key: true # Разрешает выпадение ключей с зомби
  myth_not_activated: # Сообщения, если сундук не активирован
    - ""
    - "&c✖ &bСундук не активирован &c✖"
    - ""
    - "&e🔑 Нужен ключ: &6%key%"
    - ""
  myth_activated: # Сообщения, если сундук активирован
    - ""
    - "&a✔ &6Сундук активирован! &a✔"
    - ""
  task: # Настройки времени события
    time_mode: mythical # schedule, moscow, mythical - режим работы таймера
    moscowTime: # Если выбрано moscow, время события в московском времени
      time_one: '14:40'
      time_two: '15:20'
    settings:
      pre_time_message: 15 # За сколько секунд до события показывать сообщение
      time_to_start: 10 # Через сколько секунд после появления можно открыть
      time_to_destroy: 20 # Через сколько секунд после открытия удалится
      time_to_open: 20 # Время на открытие сундука
  animations: # Анимации сундука
    locked: # Когда сундук закрыт
      anima: CREATIVE # CREATIVE, SPIRAL и другие доступные анимации
    opened: # Когда сундук открыт
      anima: SPIRAL # CREATIVE, ROD и другие доступные анимации
  bossbar: # Полоса прогресса над экраном
    opened: # Когда сундук открыт
      color: RED
      style: SOLID
      name: "&6⏳ До удаления сундука: &e%time% &6секунд &8| &a📍 Координаты: &e%x% %y% %z%"
    locked: # Когда сундук закрыт
      color: YELLOW
      style: SOLID
      name: "&6🔒 До открытия сундука: &e%time% &6секунд &8| &a📍 Координаты: &e%x% %y% %z%"
  holograms: # Голограммы над сундуком
    locked: # Когда сундук закрыт
      - "&e🔒 Сундук закрыт!"
      - "&6Открытие через: &c{time} &6секунд"
    opened: # Когда сундук открыт
      - "&a✔ &6Сундук открыт!"
      - "&6Удалится через: &c{time} &6секунд"
  rewards:
    kill_boss: #Награды первому открывшему игроку сундук
      - '[MESSAGE] &aВы первый открыли выбили уникальный ключ и получили награду!'
      - '[CONSOLE] eco give {player_name} 500'
      - '[ACTION_BAR] &6+500 монет!'
  sounds: # Звуки событий
    already_opened:
      - BLOCK_CHEST_LOCKED:3.5F:3.5F
    locked: # Когда сундук закрыт
      - BLOCK_CHEST_LOCKED:3.5F:3.5F
    opened: # Когда сундук открыт
      - MUSIC_CREDITS:3.5f:3.5F
  tools:
    location: # Настройки генерации местоположения сундука
      generate:
        world: "world" # Мир, в котором будет появляться сундук
        black_List_Biomes: # Запрещённые биомы для спавна
          - DEEP_OCEAN
          - OCEAN
          - RIVER
          - SWAMP
        black_List_Blocks: # Запрещённые блоки для спавна
          - LAVA
          - WATER
          - FIRE
          - LEAVES
          - CACTUS
          - ICE
          - SNOW
    block: # Настройки блока сундука
      setting:
        x: 0.0 # Смещение по X
        y: 1.0 # Смещение по Y
        z: 0.0 # Смещение по Z
    schematic: # Настройки схематики (если включено)
      file: drop.schem
      enable: false
    region: # Настройки региона вокруг сундука
      enable: true
      size: 20 # Радиус зоны защиты
      flags: # Флаги защиты
        - pistons: deny # Запрет на поршни
        - pvp: allow # Разрешение PvP
        - build: deny # Запрет строительства
        - mob-spawning: allow # Разрешение спавна мобов
    attempts: 100 # Количество попыток найти место
    serializate: # Границы генерации сундука
      x-direct-max: 2000
      x-direct-min: -2000
      z-direct-max: 2000
      z-direct-min: -2000
messages: # Сообщения плагина
  commands:
    command_error: "&cЭту команду может выполнять только игрок!"
    loot_menu_opened: "&aВы открыли меню лута!"
    admin_give_keys: "&bВы получили все ключи!"
    user-help:
      - "&7--------------------------"
      - "&e/airdrops loot &7- Открыть меню лута. (для админов)."
      - "&e/airdrops admin &7- Получить все ключи (для админов)."
      - "&7--------------------------"
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
  location_failure: "Не удалось сгенерировать локацию: {error}"
  incorrect_stack: "Пропущен некорректный элемент: {stack}"
  invalid_chest_location: "Некорректная локация сундука: {chestLocation}"
  schematic_in_air: "Схема может быть размещена в воздухе. Проверьте расчеты высоты."
  block_is_not_chest: "Блок на локации {chestLocation} не является сундуком!"
  quest_complete: # Сообщение при завершении квеста
    - ''
    - '&e✦ &6Все квестовые сундуки были активированы! &e✦'
    - '&a⚡ Скоро появится что-то необычное...'
  drop_spawned: # Сообщение при спавне аирдропа
    - '&e✦ &6Аирдроп успешно заспавлен на координатах: &aX: %x% &eY: %y% &aZ: %z% &e✦'
    - '&e✦ &6Содержимое: &b%rare% &e✦'
  pre_drop_spawned: # Сообщение перед появлением аирдропа
    - '&e⌛ &6Аирдроп появится примерно через: &c%time% &6секунд...'
menus:
  loot: "&e⚡ Редактирование лута сундука ⚡"
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

