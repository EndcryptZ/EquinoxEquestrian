package endcrypt.equinox.equine.transfer;

import lombok.Getter;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.Objects;

@Getter
public class EquineTransfer {

    private final Player sender;
    private final Player receiver;
    private final AbstractHorse horse;

    public EquineTransfer(Player sender, Player receiver, AbstractHorse horse) {
        this.sender = sender;
        this.receiver = receiver;
        this.horse = horse;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EquineTransfer other)) return false;
        return sender.equals(other.sender)
                && receiver.equals(other.receiver)
                && horse.equals(other.horse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, horse);
    }

}
