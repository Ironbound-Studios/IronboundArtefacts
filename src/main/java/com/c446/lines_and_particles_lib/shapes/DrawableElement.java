package com.c446.lines_and_particles_lib.shapes;

public interface DrawableElement {
    int getColor();
    int getDrawTimeInTick();
    int getTimeToLive();
    int getDecayTime();
    long getCreationTick();

    /**
     * Calcule le pourcentage de progression du dessin (0.0 à 1.0)
     * @param currentTick Le tick actuel
     * @return Le pourcentage de progression
     */
    default double getDrawProgress(long currentTick) {
        long elapsedTicks = currentTick - getCreationTick();
        if (elapsedTicks <= 0) return 0.0;
        if (elapsedTicks >= getDrawTimeInTick()) return 1.0;
        return (double) elapsedTicks / getDrawTimeInTick();
    }

    /**
     * Calcule l'alpha (transparence) basé sur le decay
     * @param currentTick Le tick actuel
     * @return L'alpha entre 0.0 (transparent) et 1.0 (opaque)
     */
    default double getAlpha(long currentTick) {
        long elapsedTicks = currentTick - getCreationTick();
        long decayStartTick = getTimeToLive();

        if (elapsedTicks < decayStartTick) {
            return 1.0; // Complètement opaque avant le début du decay
        }

        long decayElapsed = elapsedTicks - decayStartTick;
        if (decayElapsed >= getDecayTime()) {
            return 0.0; // Complètement transparent après le decay
        }

        return 1.0 - ((double) decayElapsed / getDecayTime());
    }

    /**
     * Vérifie si l'élément doit être supprimé
     * @param currentTick Le tick actuel
     * @return true si l'élément doit être supprimé
     */
    default boolean shouldRemove(long currentTick) {
        long elapsedTicks = currentTick - getCreationTick();
        return elapsedTicks >= (getTimeToLive() + getDecayTime());
    }
}