ssh-controller
==============

Android application to create a ssh controller

Current bugs

- On windows, directories and files with space may fail (need investigation) As we currently escape those characters with an antislash (linux style)
- You can select a controller when it's not connected
- It should disconnect you when you lose the connection to an access point.

TODO list:

- Wake on lan
- Better design
- Handling the connections in a better way -> asking if it should wait for a return message, if yes, we can handling it with only one connection and that would be awesome
- Possibility to change the ip address, username or password
- can connect with a public key -> not primordial but would be cool
- implements architecture for future release so that the button are still available (destroyed if serializable object change)
- enable position such as -X value -Y value
- Some main option with general settings
- Doing all the wiki for the configuration

Ideas:

- tail/less?
- action on release button
- action on repeat at an interval 
- possibility to check if the current commands are still available? Not necessary, can be a warning message
