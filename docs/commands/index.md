---
title: Commands
layout: page
nav_order: 5
has_children: false
---

# Commands

## Administrative Commands
`/pwingshops reload` - Reloads all configurations
- Permission: `pwingshops.reload`
- Options:
  - `/pwingshops reload shops` - Reloads only shop configurations
  - `/pwingshops reload menus` - Reloads only menu configurations

`/pwingshops convert` - Converts configurations from other plugins
- Permission: `pwingshops.convert`
- Options:
  - `/pwingshops convert deluxemenus` - Converts DeluxeMenus configurations
  - `/pwingshops convert shopguiplus` - Converts ShopGUI+ configurations

## Shop Commands
Each shop can have its own custom command defined in its configuration:

command: "exampleshop"  # Creates /exampleshop command

## Menu Commands

Menus can be assigned custom commands in their configurations:

command: "votemenu"     # Creates /votemenu command

Permission Structure

    pwingshops.admin - Full administrative access
    pwingshops.reload - Access to reload commands
    pwingshops.convert - Access to conversion tools
    pwingshops.shop.<shopname> - Access to specific shops
    pwingshops.menu.<menuname> - Access to specific menus
