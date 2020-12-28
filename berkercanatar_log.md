# CS102 ~ Personal Log page ~
****
## Berker Canatar
****

On this page I will keep a weekly record of what I have done for the CS102 group project. This page will be submitted together with the rest of the repository, in partial fulfillment of the CS102 course requirements.

2020-12-28, work done: Merge pull request #24 from berkercanatar/formation-dev<br>
2020-12-28, work done: COMPLETE<br>
2020-12-28, work done: Swarm implementation
2020-12-28, work done: Merge pull request #23 from berkercanatar/connecteddrones-dev
2020-12-28, work done: Merge branch 'master' into connecteddrones-dev
2020-12-28, work done: Minor improvements
2020-12-28, work done: Connected Drone panels implementation
2020-12-28, work done: Fİx mission start state conditions
2020-12-28, work done: Merging and combining all the works done at branches
2020-12-28, work done: Merge pull request #20 from berkercanatar/Slider
2020-12-28, work done: Merge branch 'master' into Slider
2020-12-28, work done: Merge pull request #21 from berkercanatar/popupscreen-dev
2020-12-28, work done: Merge pull request #22 from berkercanatar/flightplan-dev
2020-12-28, work done: Flight mission planning implementation completed!!
2020-12-28, work done: Merge pull request #19 from berkercanatar/help-page
2020-12-28, work done: Update TabController.java
2020-12-28, work done: Popup code base improvement
2020-12-28, work done: Slider code base
2020-12-28, work done: Merge pull request #17 from berkercanatar/popupscreen-dev
2020-12-28, work done: Merge branch 'master' into popupscreen-dev
2020-12-28, work done: Merge pull request #16 from berkercanatar/Slider
2020-12-28, work done: Merge branch 'master' into Slider
2020-12-27, work done: Minor improvement
2020-12-27, work done: Add missing icons
2020-12-27, work done: Minor improvement
2020-12-27, work done: Update AutopilotControlPanel.java
2020-12-27, work done: Merge pull request #14 from berkercanatar/telemetry-dev
2020-12-27, work done: Telemetry panel + initial flight mission planning implementation
2020-12-27, work done: Implement telemetry panel
2020-12-27, work done: Add pitch icon
2020-12-27, work done: Add buttons for new commands
2020-12-27, work done: Add new states and conditions
2020-12-27, work done: Draw red border on command buttons when function is not available
2020-12-27, work done: Improve swarm flight
2020-12-26, work done: Swarm implementation
2020-12-26, work done: Merge branch 'master' of https://github.com/berkercanatar/Javigation
2020-12-26, work done: Merge pull request #13 from berkercanatar/formation-dev
2020-12-26, work done: Merge pull request #12 from berkercanatar/formation-dev
2020-12-26, work done: Receive connection from 3 drones
2020-12-26, work done: Fix connection issue
2020-12-26, work done: Merge pull request #10 from berkercanatar/popupscreen-dev
2020-12-26, work done: Merge pull request #11 from berkercanatar/flightplan-dev
2020-12-26, work done: Improve connectivity
2020-12-26, work done: Activate HOLD mode when drone is not being controlled manually and has very low linear speed by deactivating OFFBOARD mode
2020-12-26, work done: Add manual position control. Clicking on map now navigates the drone to the clicked location at same altitude.
2020-12-26, work done: Add function to find bearing
2020-12-26, work done: Add HOLD mode, add heading control to GoTo command
2020-12-26, work done: Add manual control state, fix click passthrough from overlapping panels to underlying map
2020-12-26, work done: Merge pull request #9 from berkercanatar/controlpanel-dev
2020-12-26, work done: Update button functions when event triggered, instead of checking with timer at fixed interval. Add RTL button.
2020-12-26, work done: Add new states, improved conflict detecting mechanism
2020-12-26, work done: Listen for any state changes of drone
2020-12-26, work done: Create StateChangedListener.java
2020-12-25, work done: Move DroneConnection, change references accordingly
2020-12-25, work done: Change MAVSDK-Server output redirection target from System.err to System.out
2020-12-25, work done: Add feautre to show heading value on the map, i.e. rotating drone icons on the map
2020-12-25, work done: Do telemetry subscriptions ad Drone Controller class
2020-12-25, work done: Optimize map drawing FPS
2020-12-25, work done: Add method for rotating images
2020-12-25, work done: Fix issue caused by blocking first, implement status checking using state machine
2020-12-25, work done: Add fields for Armed and InAir statuses
2020-12-25, work done: Subscribe and listen for changes in armed, preflight check, in air statuses and store variables in Telemetry class
2020-12-25, work done: Check for conflicts in state machine, add string representation of current state machine
2020-12-25, work done: Merge pull request #8 from berkercanatar/controlpanel-dev
2020-12-25, work done: Update Drone Controller
2020-12-25, work done: Add Offboard control for manual controls
2020-12-24, work done: Update DronePainter.java
2020-12-24, work done: Automatically start painting the drone when connected
2020-12-24, work done: Fix painter and control button background
2020-12-24, work done: Fix README.md
2020-12-24, work done: Merge pull request #7 from berkercanatar/formation-dev
2020-12-24, work done: Improve control panel buttons. Improved visuals (alignment, background border etc.)
2020-12-24, work done: Fix issue about trying to paint drone even though it is not connected
2020-12-24, work done: Update drone control panel with better alignment
2020-12-24, work done: Create ControlStickButtons.java
2020-12-24, work done: Make connection and isDroneConnected variables public in order to check if drone is connected before painting its icon on the map
2020-12-24, work done: Make containers background transparent
2020-12-24, work done: Enable hardware accelerated graphics drawing
2020-12-24, work done: Fix wrong naming in pitch icons
2020-12-23, work done: Merge pull request #6 from berkercanatar/controlpanel-dev
2020-12-22, work done: Fix location of control panel
2020-12-22, work done: Switch between camera and map view
2020-12-21, work done: Update README.md
2020-12-22, work done: Merge pull request #4 from berkercanatar/splashscreen
2020-12-22, work done: Update README.md
2020-12-22, work done: Draw drone icon on the map in real time according to the position telemetry value from mavlink packets
2020-12-22, work done: Subscribe for telemetry, dynamic MavSDK port
2020-12-22, work done: Subscribe for position telemetry value of connected drones and update in a local object
2020-12-22, work done: Add map FPS control
2020-12-22, work done: Skip splash screen when debugging
2020-12-22, work done: Add lastpainttime in order to manage FPS of the map
2020-12-19, work done: Fix issue with loading control panel icons
2020-12-19, work done: Fix imports
2020-12-19, work done: Minor fix on control panel
2020-12-19, work done: Update .gitignore
2020-12-19, work done: Move gstreamer files to video folder
2020-12-18, work done: Add flight mission feature base
2020-12-18, work done: Add class to plan simple mission with single line
2020-12-18, work done: Define all commands
2020-12-17, work done: Move log files to personal-logs branch
2020-12-17, work done: Add functionality to automatically download, install and setup GStreamer-1.18.2
2020-12-17, work done: Create Statics.java
2020-12-17, work done: Add class that downloads gstreamer automatically
2020-12-17, work done: Add JNA-Platform library to project to get access to native OS functions
2020-12-17, work done: Merge branch 'master' of https://github.com/berkercanatar/Javigation
2020-12-17, work done: Add log for berkercanatar
2020-12-17, work done: Merge pull request #2 from berkercanatar/controlpanel-dev
2020-12-11, work done: Merge pull request #1 from berkercanatar/swarmcontrol-dev
2020-12-11, work done: Create SwarmControlPanel.java
2020-12-11, work done: Remove unnecessary imports
2020-12-11, work done: Add custom panel shape
2020-12-11, work done: Update GUIManager.java
2020-12-11, work done: Update RoundedBorder to draw custom background
2020-12-11, work done: Fix location of DroneControlPanel
2020-12-11, work done: Update drone control features
2020-12-11, work done: GUI Updates
2020-12-11, work done: Add utils that find elements in given ArrayList
2020-12-11, work done: Add linux version of MAVSDK
2020-12-07, work done: GStreamer implementation
2020-12-07, work done: Add new tab icons
2020-12-06, work done: Update .gitignore
2020-12-06, work done: Move GUI creater to another class, add app icon
2020-12-06, work done: Add Tab Controller
2020-12-06, work done: Add Route Painter and Flight Mission
2020-12-06, work done: Add drone icons and drawer
2020-12-06, work done: Add gstreamer library dependency
2020-12-06, work done: Add gstreamer library
2020-12-06, work done: Mavsdk Server update
2020-12-06, work done: Update map icons
2020-11-22, work done: DroneController sample
2020-11-22, work done: Add MAVSDK library to project files
2020-11-22, work done: Add MAVSDK binary and some notes
2020-11-22, work done: Add MAVSDK library to project
2020-11-22, work done: Implement memory management
2020-11-22, work done: Add listener functionality to distinguish right and left clicks
2020-11-22, work done: Fix zoom in Bing Map provider
2020-11-22, work done: Code cleanup
2020-11-22, work done: Fix MAX zoom in OSM provider
2020-11-22, work done: Fix zoom in Google Map provider
2020-11-22, work done: Add TileCleaner that manages memory cleaning
2020-11-20, work done: MapView implemented
2020-11-20, work done: Add Terrain, Satellite and Hybrid tiles in MapProvider
2020-11-20, work done: Add function to generate tile URL from lat, lon for specific zoom level
2020-11-20, work done: Initial commit
