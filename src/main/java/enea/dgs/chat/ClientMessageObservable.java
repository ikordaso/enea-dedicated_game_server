package enea.dgs.chat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * Singleton class representing an observable notifying attached observers about changes in its
 * state.
 * <p>
 * Uses {@link PropertyChangeSupport} and {@link PropertyChangeListener} at its core.
 */
public class ClientMessageObservable {

    private static final ClientMessageObservable INSTANCE = new ClientMessageObservable();
    private final PropertyChangeSupport observableSupport;

    /**
     * Constructor instantiating observable support.
     */
    private ClientMessageObservable() { this.observableSupport = new PropertyChangeSupport(this); }

    /**
     * Returns singleton instance.
     *
     * @return {@link ClientMessageObservable}
     */
    public static ClientMessageObservable getInstance() {
        return INSTANCE;
    }

    /**
     * Attaches the provided listener on the observable bus. Will be notified when an event comes
     * matching the provided key.
     * <p>
     * The same listener object may be added more than once. For each key, the listener will be
     * invoked the number of times it was added for that key. If key or listener is {@code null}, no
     * exception is thrown and no action is taken.
     *
     * @param listener {@link ClientMessageObserver} instance
     */
    public void addObserver(final ClientMessageObserver listener) {
        observableSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove the provided listener from the observable bus.
     *
     * @param listener {@link ClientMessageObserver} instance
     */
    public void removeObserver(final ClientMessageObserver listener) {
        observableSupport.removePropertyChangeListener(listener);
    }


    /**
     * Triggers broadcast to every attached observer (client).
     * <p>
     * If any of the provided parameters is {@code null}, firing of filed event will not be triggered.
     *
     */
    public void broadcastMessageFrom(final String clientIdentifier, final String message) {
        if (!isNullOrEmpty(clientIdentifier) && !isNullOrEmpty(message))
            observableSupport.firePropertyChange(clientIdentifier, clientIdentifier, message);
    }

    /**
     * Returns whether the provided String value is null or empty.
     *
     * @param value {@link String} instance
     * @return {@code boolean} flag
     */
    public static boolean isNullOrEmpty(final String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

}
