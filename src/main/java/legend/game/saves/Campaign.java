package legend.game.saves;

public record Campaign(String filename, SavedGame<?> latestSave) {

}
