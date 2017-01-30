/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;


/**
 * @author Hubert
 *
 */
public abstract class AbstractTileHandler implements Runnable {

	private ArrayList<QueueObserver> queueObservers = new ArrayList<QueueObserver>();
	
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
	
	private BlockingQueue<AbstractTileCommand> commandQueue;
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

	private boolean stopped = false;

	
    public void start() {
    	new Thread(this).start();
    }
    
    public void stop() {
    	stopped = true;
    	try {
			commandQueue.put(new StopCommand());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			//System.out.println("Command added to queue: "+command.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!stopped) {
			try {
				AbstractTileCommand command = commandQueue.take();
				notifyQueueObservers();
				command.doAction();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
