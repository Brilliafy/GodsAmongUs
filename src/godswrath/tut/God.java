package godswrath.tut;

import org.bukkit.entity.Player;


public interface God {

    public void Invoke(Player player);
    public void requestQuest(Player player);
    public void invokeQuest(Player player, Boolean Completed);

}
