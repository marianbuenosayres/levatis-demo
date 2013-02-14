package com.plugtree.levatis.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Properties;

import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.Environment;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;

import com.plugtree.levatis.ext.TestWorkItemHandler;
import com.plugtree.levatis.ext.TestWorkItemManagerFactory;

public class ExampleStrategyWorkItemHandlerTest {

	@Test
	public void testChangingWorkItemManager() throws Exception {
		Properties sessionProps = new Properties();
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		Environment env = KnowledgeBaseFactory.newEnvironment();
		kbuilder.add(new ClassPathResource("process-demo.bpmn2"), ResourceType.BPMN2);
		if (kbuilder.hasErrors()) {
			for (KnowledgeBuilderError error : kbuilder.getErrors()) {
				System.out.println(error);
			}
			throw new IllegalArgumentException("Problem parsing knowledge base!!!");
		}
		
		//this configures a class to create your custom WorkItemManager
		sessionProps.setProperty("drools.workItemManagerFactory", TestWorkItemManagerFactory.class.getName());
		KnowledgeSessionConfiguration ksconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(sessionProps);
		StatefulKnowledgeSession ksession = kbuilder.newKnowledgeBase().newStatefulKnowledgeSession(ksconf, env);
		//END this configures a class to create your custom WorkItemManager
		
		TestWorkItemHandler testHandler = new TestWorkItemHandler();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", testHandler);
		
		ProcessInstance procInstance = ksession.startProcess("levatis.process-demo");
		assertEquals(ProcessInstance.STATE_ACTIVE, procInstance.getState());
		ksession.getWorkItemManager().completeWorkItem(testHandler.getId(), new HashMap<String, Object>());
		assertEquals(ProcessInstance.STATE_ACTIVE, procInstance.getState());
		ksession.getWorkItemManager().completeWorkItem(testHandler.getId(), new HashMap<String, Object>());
		assertEquals(ProcessInstance.STATE_COMPLETED, procInstance.getState());
	}
}

