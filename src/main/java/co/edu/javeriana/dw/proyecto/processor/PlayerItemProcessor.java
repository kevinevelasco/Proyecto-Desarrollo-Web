package co.edu.javeriana.dw.proyecto.processor;

import co.edu.javeriana.dw.proyecto.model.Player;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.logging.Logger;

public class PlayerItemProcessor implements ItemProcessor<Player, Player> {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(PlayerItemProcessor.class);
    @Override
    public Player process(Player item) throws Exception {
        String user = item.getUserName().toUpperCase();
        String password = item.getPassword().toUpperCase();
        String type = item.getType().toUpperCase();

        Player transformedPlayer = new Player(user, password, type);

        LOG.info("Converting (" + item + ") into (" + transformedPlayer + ")");
        return transformedPlayer;
    }
}
