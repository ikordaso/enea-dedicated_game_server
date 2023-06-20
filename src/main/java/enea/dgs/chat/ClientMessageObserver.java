package enea.dgs.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class representing an observer watching for messages from other clients.
 */
public class ClientMessageObserver implements PropertyChangeListener {

    private final BiConsumer<String, String> messageConsumer;

    /**
     * Constructor accepting key to watch.
     *
     * @param messageConsumer {@link Consumer} instance - logic executed when some client message is received
     */
    private ClientMessageObserver(final BiConsumer<String, String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    /**
     * Factory method constructor accepting the provided key which is used to attach to observation
     * bus (listen events linked to it).
     *
     * @return {@link ClientMessageObserver} instance
     */
    public static ClientMessageObserver of(final BiConsumer<String, String> messageConsumer) {
        return new ClientMessageObserver(messageConsumer);
    }

    /**
     * Attaches the observer to the observable bus.
     */
    public void attach() { ClientMessageObservable.getInstance().addObserver(this); }

    /**
     * Detaches the observer from the observable bus.
     */
    public void detach() { ClientMessageObservable.getInstance().removeObserver(this); }

    /**
     * In case the provided logic is not {@code null}, observer attaches to the  observable bus,
     * triggers logic execution and finishes with observer detachment from the observable bus.
     *
     * @param logic {@link Runnable} instance
     */
    public void executeWrapped(Runnable logic) {
        if (Objects.isNull(logic))
            return;

        attach();
        logic.run();
        detach();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Executes set message consumer with the triggered event values.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
        if (Objects.nonNull(propertyChangeEvent) && Objects.nonNull(propertyChangeEvent.getNewValue()))
            messageConsumer.accept((String) propertyChangeEvent.getOldValue(),
                    (String) propertyChangeEvent.getNewValue());
    }

}
