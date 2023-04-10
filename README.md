# LiveSplit Hooks

An auto-splitter/config/util mod for Necesse that pairs with [LiveSplit](https://livesplit.org/)

## Features

- Configurable auto-splitting
- Custom new world setting overrides (i.e. difficulty/starting island/etc.)
- Game time tracking in LiveSplit (removes level loading times)

## How to use this mod

This mod requires the [LiveSplit Server](https://github.com/LiveSplit/LiveSplit.Server) plugin to be downloaded and running.

### Install the LiveSplit Server plugin

Navigate to the [downloads page](https://github.com/LiveSplit/LiveSplit.Server/releases) and download the latest `LiveSplit.Server.zip`.
Locate the LiveSplit installation directory (i.e. `C:\Program Files\LiveSplit_1.8.25`) and extract the contents of the `.zip` file into the `Components` directory.

### Add the Server plugin

In the LiveSplit layout editor, find the Server control (under `Control > LiveSplit Server`) and add it to the layout anywhere.
You shouldn't need to change the port number but if you do, make sure to edit the options for this mod to match.

### Connect Necesse to the LiveSplit Server

Boot up Necesse and check the LiveSplit icon in the main menu icon tray (bottom right).
If the icon is green, then all is good and speedrunning can begin!
If the icon is red, ensure that the LiveSplit Server is running (`LiveSplit context menu > Control > Start Server`)
Once the LiveSplit Server is up and running, press the LiveSplit icon in Necesse to attempt to reconnect.
If you have changed the default port for the LiveSplit Server, open the Necesse Settings menu and click the `LiveSplit` entry.
The port number can then be configured using the top option.

### Start a new world and go fast

The LiveSplit timer will automatically be reset/started when creating a new world.
The timer is also stopped once the final segment is completed.
