package endcrypt.equinox.equine.transfer;

import lombok.Getter;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EquineTransfer that = (EquineTransfer) o;

        return sender.equals(that.sender)
                && receiver.equals(that.receiver)
                && horse.equals(that.horse);
    }

    @Override
    public int hashCode() {
        int result = sender.hashCode();
        result = 31 * result + receiver.hashCode();
        result = 31 * result + horse.hashCode();
        return result;
    }
}
