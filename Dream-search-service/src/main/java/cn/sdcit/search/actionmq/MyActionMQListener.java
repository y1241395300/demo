package cn.sdcit.search.actionmq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyActionMQListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		
		try {
			TextMessage textMessage= (TextMessage) message;
			String text = textMessage.getText();
			System.out.println(text);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
