# ConfigSys
Using XML/XSLT for more powerful System Engineering

Say you have a lot of servers, big cloud, Hadoop, cluster forming a busy production web site. Odds are pretty good almost all of them fall into one of several categories such as Web, DBMS, Application, Datanodes/Task Trackers and it's also very likely all of those are running the same software components with only a few words and values scattered across each in a dozen or more places, all in differently formatted files with no coordination in between other than a careful System Administrator edited them and that's about all that actually separate one host from another.
You hatch another server whether virtual or real and then log in and edit all the files with a text editor to suit it's purpose every time you need one and if a server dies you start from scratch. Mistakes are made, maybe you skip a step or an entire file or just do it a bit differently and then start the debugging and head scratching. Once inconsistencies are introduced the smooth upgrade path and migration forward just became a Herculean task of compound frustration and manual labor instead of the automated sheer bliss it should be.
This project introduces the idea of centralizing all the information that separates them into a central XML file store and then by applying XSLT templates to create configuration files and even scraps of script to configure hosts in a reliable, repeatable fashion and aid in automating server build, software installation, deployments, maintenance and even moving builds from zone to zone with predictable results and lower the cost and possible errors of human intervention.
