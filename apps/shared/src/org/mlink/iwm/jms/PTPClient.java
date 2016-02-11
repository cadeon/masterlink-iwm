package org.mlink.iwm.jms;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.EnvUtils;

import javax.naming.NamingException;
import javax.naming.Context;
import javax.jms.*;
import java.io.Serializable;

/**
 * User: andreipovodyrev
 * Date: Jul 5, 2007
 * PointToPoint JMS client. Both sender and Receiver. Uses JBOSS default queue
 */
public class PTPClient implements MessageListener {
    private static final Logger logger = Logger.getLogger(PTPClient.class);
    private static PTPClient me = new PTPClient();
    QueueConnection conn;
    QueueSession session;
    Queue queue;


    private static PTPClient getInstance(){
        return me;
    }

    public static void start() throws JMSException{
        getInstance().setupPTP();
        log("Started");
    }

    public static void stop() throws JMSException{
        getInstance()._stop();
        log("Stopped");
    }

    public static void sendObjectAsync(Serializable msg)  {
        try {
            if(getInstance().conn==null) getInstance().setupPTP();
            getInstance()._sendObjectAsync(msg);
        } catch (JMSException e) {
            handleExc(e);
        }
    }

    public static void sendTextAsync(String msg) {
        try {
            if(getInstance().conn==null) getInstance().setupPTP();
            getInstance()._sendTextAsync(msg);
        } catch (JMSException e) {
            handleExc(e);
        }
    }

    private static void handleExc(Throwable e){
        e.printStackTrace();
    }


    private PTPClient() {}

    public void setupPTP() throws JMSException{
        try {
            Context ctx = EnvUtils.getInitialContext();
            logger.debug("Looking up Connection Factory");
            Object tmp = ctx.lookup("ConnectionFactory");      //jboss default
            QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
            logger.debug("Creating connection");
            conn = qcf.createQueueConnection();
            logger.debug("Looking up queue/testQueue");
            queue = (Queue) ctx.lookup("queue/testQueue");     //jboss default
            logger.debug("Creating session");
            session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            session.createReceiver(queue).setMessageListener(this);
            conn.start();
            //conn.setExceptionListener(new PTPClientConnectionListener());
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void _stop() throws JMSException{
        log("Cleaning session");
        try {
            conn.stop();
            session.close();
            conn.close();
        } catch (NullPointerException e) {
            // no problem, never been started
        }
    }

    private void _sendTextAsync(String text) throws JMSException {
        log("Sending " + text);
        QueueSender sender = session.createSender(queue);
        TextMessage tm = session.createTextMessage(text);
        sender.send(tm);
        log("Message sent");
        sender.close();
    }

    private void _sendObjectAsync(Serializable msg) throws JMSException {
        log("Sending object" + msg);
        QueueSender sender = session.createSender(queue);
        ObjectMessage om = session.createObjectMessage(msg);
        sender.send(om);
        log("Message sent");
        sender.close();
    }


    public void onMessage(Message message) {
        try {
            if(message instanceof TextMessage){
                TextMessage tm = (TextMessage) message;
                log("Receiving message " + tm.getText());
            }else if( message instanceof ObjectMessage){
                process((ObjectMessage)message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //todo need exception handling
        }
    }

    private void process(ObjectMessage message) throws Exception{
        Object object = message.getObject();
        if(object instanceof Command){
            ((Command)object).execute();
        }else{
            log("Object message not supported " + object);
        }
        log("processed message " + object);
    }

    private static void log(String msg){
        logger.debug(msg);
    }


    /**
     * JBoss must be up and running whle executing this main() method
     * @param args
     */
    public static void main(String args[]){
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        /*For logging make sure you run this class with log4j setting like below
        -Dlog4j.debug=true -Dlog4j.configuration=file:/Users/andreipovodyrev/Masterlink/iwm_v35/apps/config/iwm-log4j.xml
        */
        try {
            PTPClient.log("starting");
            PTPClient.start();
            PTPClient.sendTextAsync("hello");

            //see jboss output for response

            //PTPClient.stop();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    class PTPClientConnectionListener implements ExceptionListener{
        public void onException(JMSException jmsException) {
            logger.debug("Testing Listener");
            try{
                setupPTP();
            }catch(JMSException e){
                e.printStackTrace();
            }
        }
    }
}
