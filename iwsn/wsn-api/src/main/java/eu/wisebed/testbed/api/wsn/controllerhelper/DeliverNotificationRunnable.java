package eu.wisebed.testbed.api.wsn.controllerhelper;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import eu.wisebed.api.controller.Controller;

class DeliverNotificationRunnable extends DeliverRunnable {

	private final List<String> notifications;

	DeliverNotificationRunnable(final ScheduledThreadPoolExecutor scheduler,
								final DeliverFailureListener failureListener,
								final String controllerEndpointUrl,
								final Controller controllerEndpoint,
								final List<String> notifications) {

		super(scheduler, failureListener, controllerEndpointUrl, controllerEndpoint);
		this.notifications = notifications;
	}

	@Override
	protected void deliver(final Controller controller) {
		controller.receiveNotification(notifications);
	}
}