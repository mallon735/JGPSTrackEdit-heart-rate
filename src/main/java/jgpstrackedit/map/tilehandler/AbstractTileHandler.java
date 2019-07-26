/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Hubert
 *
 */
public abstract class AbstractTileHandler implements Runnable 
{
	private static Logger logger = LoggerFactory.getLogger(AbstractTileHandler.class);
	
	private List<QueueObserver> queueObservers = new LinkedList<>();
	private boolean stopped = false;
	private BlockingQueue<AbstractTileCommand> commandQueue = new LinkedBlockingQueue<>();
	
	public synchronized void addQueueObserver(QueueObserver queueObserver) {
		queueObservers.add(queueObserver);
	}
	
	public synchronized void removeQueueObserver(QueueObserver queueObserver) {
		queueObservers.remove(queueObserver);
	}
	
	protected synchronized void notifyQueueObservers() {
		for (QueueObserver observer:queueObservers) {
			observer.lengthChanged(commandQueue.size());
		}
	}
	
    /**
	 * @return the commandQueue
	 */
	protected BlockingQueue<AbstractTileCommand> getCommandQueue() {
		return commandQueue;
	}

	/**
	 * @param commandQueue the commandQueue to set
	 */
	protected void setCommandQueue(
			BlockingQueue<AbstractTileCommand> commandQueue) {
		this.commandQueue = commandQueue;
	}
	
    public void start() {
    	new Thread(this).start();
    }
    
    public void stop() {
    	stopped = true;
    	try {
			commandQueue.put(new StopCommand());
		} catch (InterruptedException e) {
			logger.error("Error while stopping thread!", e);
		}
    }
    
    /** Adds a command to be executed later on. A child class
     * should overload this method (or create a new one with other name)
     * as public for a concrete 
     * specific command to be added, using this method.
     * 
     * @param command
     */
    protected void addCommand(AbstractTileCommand command) {
    	try {
			commandQueue.put(command);
			notifyQueueObservers();
		} catch (InterruptedException e) {
			logger.error("Error while adding command!", e);
		}
    	
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!stopped) {
			try {
				final AbstractTileCommand command = commandQueue.take();
				notifyQueueObservers();
				command.doAction();
			} catch (InterruptedException e) {
				logger.error("Error while running thread!", e);
			}
		}
	}
	

}
