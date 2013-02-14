Levatis Demo
============

**This is a small demo of how to access through this demonstration.**
You will find two folders:

levatis-case (which is a maven project with all the compilable contents)
deployments (which is the deployment folder of a deployed version of the contents on a JBoss7)

deployments contains a small implementation of integration between a custom webapp and the jbpm designer. All the implementation is inside levatis-case app. The war it compiles should be replacing the folder levatis-demo.war that is deployed in deployments. The designer.war should replace the one in deployments as well, but the profiles/jbpm.xml file needs to stay like that. All configurations of points in common needed are in the web.xml of the levatis-case app, and that is a good point to start looking through that application.
Also, levatis-case contains a test case called ExampleStrategyWorkItemHandlerTest, that shows how to implement a custom WorkItemHandler strategy and a custom WorkItemManager

The only change done to designer.war is the change of the webapp name in file designer.war/profiles/jbpm.xml, as mentioned in our call, and no extra configuration was changed from the JBoss7 than the deployments folder.

There is a class called Repository inside levatis-case webapp, that is to be replaced by something useful for you. That would be the common point with your repository that needs implementation. Currently all it does is give you one single process (process-demo.bpmn2) and when you try to save it, it prints it through the screen.

Interfaces used by both the project and the designer are the rest service (implemented in web.xml and GuvnorRESTEmulator) and the OryxEditorServlet. 

If you want to continue working on these, feel free to fork it and play with it.
