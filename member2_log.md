# CS102 ~ Personal Log page ~
****
## Hakan Gülcü
****

On this page I will keep a weekly record of what I have done for the CS102 group project. This page will be submitted together with the rest of the repository, in partial fulfillment of the CS102 course requirements.

### ~ 17.12.2020~
This week I searched swarm algorithms and optimization for our group of drones. I found some articles to understand the logic behind it. 
Also, I started to design the algorithm for our program.

### ~ 21.12.2020-27.12.2020~
This week, I coded flight part. In formation, there are 2 different types positioning, triangle form and horizontal form. Because we swarm with 3 drones, those are enough. In formation, I took different formulas such as Haversine for distance between 2 points in earth with latitude and longitude. If leader is not selected by user but swarm is wanted, we look their position and choose the leader at the middle. Follower drones are coming to assigned position. To avoid collisions, we created virtual points according to leader position. After that, we calculate the distance of the follower drones to these positions separately and make them go to the appropriate position. 

We dont just formation so in swarm, our drones should we together according to leader's position. Therefore, after formation, we created a constant positioning between leader and followers. According to given task, leader goes to position and folllowers follows it by information coming from leader. Turgut helped me in these parts. 

I am doing slider for confirmation of tasks. For any misclick, it is important to have a criteria to start action and go commandchain. 


### ~ date ~
Together with Berker, Turgut and Furkan, we researched to create the command chain of the keys. We have edited the commandchain due to the difficulties that can occur with sudden inputs. In the coding part, I helped research and apply methods. Also, in GUI part, i gave some ideas and helped them.

### ~ date ~
blah, blah, blah...

****
