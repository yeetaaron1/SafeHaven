# Project Name: SafeHaven

## Project Overview
**Description:**  
SafeHaven is a customizable home system plugin for Minecraft, designed to offer players and administrators a flexible and feature-rich way to manage homes. The plugin supports different storage options (MySQL, SQLite, or YAML) and provides highly customizable messages, commands, and GUI menus. The plugin aims to enhance the player experience with an easy-to-use interface and powerful admin tools.

## Features

1. **Player Commands:**
    - `/sethome [name]`: Set a new home with a specified name.
    - `/home [name]`: Teleport to a specified home.
    - `/homes`: List all homes set by the player.
    - `/delhome [name]`: Delete a specified home.

2. **Admin Commands:**
    - `/safehaven sethome <player> [name]`: Set a home for a specific player.
    - `/safehaven home <player> [name]`: Teleport to a specific player's home.
    - `/safehaven delhome <player> [name]`: Delete a home for a specific player.
    - `/safehaven list <player>`: List all homes of a specific player.

3. **GUI Menus:**
    - **Player GUI**:
        - Manage homes through an intuitive GUI interface.
        - Options to set, delete, and teleport to homes via the menu.
    - **Admin GUI**:
        - View and manage all player homes.
        - Administer player homes, teleport to specific homes, or delete them.

4. **Configuration System:**
    - **Storage Options**:
        - **MySQL**: Store player home data in a MySQL database.
        - **Mongodb**: Store player home data in a Mongodb database.
        - **SQLite**: Use an SQLite database for local storage.
        - **YAML Files**: Save each player's home data in individual YAML files.
    - **Configurable Settings**:
        - **`config.yml`**:
            - Define the default number of homes a player can set.
            - Set limits for the maximum number of homes per player.
            - Customize all command messages and error messages.
            - Select the storage method (MySQL, SQLite, or YAML).

## Project Plan

### _~~Phase 1: Initial Setup~~_ Task: Finished
- **_~~Repository Setup~~_**: _~~Create a public GitHub repository for version control and community collaboration.~~_
- **_~~Development Environment~~_**: _~~Prepare your development environment (Java, Maven, IDE).~~_
- **_~~Plugin Structure~~_**: _~~Set up the base structure of the plugin, including package organization, and create the main class. Define the `plugin.yml`.~~__

### Phase 2: Core Functionality
- **_~~Player Commands Implementation~~_**: Task: Finished (Mostly Working)
    - _~~Develop the commands for players (`/sethome`, `/home`, `/homes`, `/delhome`, `/homegui`)._~~
    - _~~Implement basic home management functionality, including saving and retrieving homes.~~_

- **Admin Commands Implementation**: Task: 50/100 (Basic Format)
    - Create the admin commands (`/safehaven help`,`/safehaven sethome <username>`, `/safehaven home <username>`, `/safehaven delhome <username>`, `/safehaven list <username>`, `/safehaven admingui`).
    - Implement admin functionalities for managing player homes.

### Phase 3: Storage and Configuration
- **_~~Storage System~~_**: Task: Finished (Mostly working, One issue you have to restart the server if you want to change the storage system.)
    - _~~Implement the storage system, allowing data to be saved in MySQL, SQLite, Mongodb, or YAML files.~~_
    - _~~Ensure the plugin can switch between different storage options based on configuration.~~_

- **Config System**:
    - Develop the `config.yml` to include settings for home limits, messages, and storage options.
    - Implement customizable messages and limits, allowing server admins to tailor the plugin to their needs.

### Phase 4: GUI Development
- **Player GUI**: Task: Mostly Finished (So far it works but players cant set a home in the Gui Menu)
    - _~~Design and implement a GUI menu for players to manage their homes visually.~~_
    - Add features for setting, deleting, and teleporting to homes via the GUI. (Setting Isn't working)

- **Admin GUI**: Task: Not Working
    - Create an admin-specific GUI for managing all player homes.
    - Include options for viewing, teleporting, and deleting homes.

### Phase 5: Testing and Optimization
- **Beta Testing**: Not Planned Yet
    - Test all features internally and fix any bugs.
    - Release a beta version to gather feedback from a broader audience.

- **Optimization**: Not Planned Yet
    - Ensure the plugin runs smoothly with different storage options.
    - Optimize for performance, particularly when using databases.

### Phase 6: Documentation and Release
- **Documentation**: Not Planned Yet
    - Write detailed documentation for users, covering installation, configuration, and command usage.
    - Create guides for setting up different storage options.
    - Add Language Support.

- **Release**: Only Published to Modrinth and GitHub.
    - Publish the plugin on platforms like SpigotMC, Modrinth and GitHub.
    - Promote the release through forums, Discord communities, and social media.

- **Ongoing Support**: 
    - Monitor user feedback and respond to issues.
    - Provide regular updates and improvements based on community input.
 