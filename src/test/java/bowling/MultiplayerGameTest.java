package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiplayerGameTest {

    private PartieMultijoueur partie;

    @BeforeEach
    public void setUp() {
        partie = new PartieMultijoueur();
    }

    /**
     * Teste le démarrage d'une partie avec plusieurs joueurs
     */
    @Test
    void demarrerPartie() {
        String[] joueurs = {"Joris", "Riyad"};
        String message = partie.demarreNouvellePartie(joueurs);
        
        assertEquals("Prochain tir : joueur Joris, tour n° 1, boule n° 1", message);
    }

    /**
     * Teste le cas où la partie n'a pas démarré et qu'on tente d'enregistrer un lancer
     */
    @Test
    void lancerAvantDemarrerPartie() {
        assertThrows(IllegalStateException.class, () -> {
            partie.enregistreLancer(5);
        });
    }

    /**
     * Teste le cas où on enregistre plusieurs lancers avec 2 joueurs
     */
    @Test
    void enregistrerLancersMultiJoueurs() {
        String[] joueurs = {"Joris", "Riyad"};
        partie.demarreNouvellePartie(joueurs);

        // Joris lance
        String message = partie.enregistreLancer(5);
        assertEquals("Prochain tir : joueur Riyad, tour n° 1, boule n° 1", message);

        // Riyad lance
        message = partie.enregistreLancer(7);
        assertEquals("Prochain tir : joueur Joris, tour n° 2, boule n° 1", message);

        // Joris lance à nouveau
        message = partie.enregistreLancer(8);
        assertEquals("Prochain tir : joueur Riyad, tour n° 2, boule n° 1", message);

        // Riyad lance à nouveau
        message = partie.enregistreLancer(10);
        assertEquals("Prochain tir : joueur Joris, tour n° 3, boule n° 1", message);
    }

    /**
     * Teste le score d'un joueur après avoir enregistré des lancers
     */
    @Test
    void scoreJoueur() {
        String[] joueurs = {"Joris", "Riyad"};
        partie.demarreNouvellePartie(joueurs);

        // Joris lance
        partie.enregistreLancer(5);
        partie.enregistreLancer(3);

        // Riyad lance
        partie.enregistreLancer(7);
        partie.enregistreLancer(2);

        // Vérifier le score de chaque joueur
        assertEquals(8, partie.scorePour("Joris"));
        assertEquals(9, partie.scorePour("Riyad"));
    }

    /**
     * Teste la fin de la partie pour un joueur
     */
    @Test
    void finPartie() {
        String[] joueurs = {"Joris", "Riyad"};
        partie.demarreNouvellePartie(joueurs);

        // Joris fait un strike
        partie.enregistreLancer(10);

        // Riyad fait un strike
        partie.enregistreLancer(10);

        // On enregistre 18 autres lancers pour terminer les tours
        for (int i = 0; i < 18; i++) {
            partie.enregistreLancer(0);
        }

        assertEquals("Partie terminée", partie.enregistreLancer(0));  // Dernier lancer

        // Vérifier les scores à la fin de la partie
        assertTrue(partie.scorePour("Joris") >= 10);  // Score de Joris après un strike
        assertTrue(partie.scorePour("Riyad") >= 10);    // Score de Riyad après un strike
    }

    /**
     * Teste la gestion des joueurs inexistants
     */
    @Test
    void joueurInexistant() {
        String[] joueurs = {"Joris", "Riyad"};
        partie.demarreNouvellePartie(joueurs);

        // Tester l'exception si le joueur n'existe pas
        assertThrows(IllegalArgumentException.class, () -> {
            partie.scorePour("Charlie");
        });
    }

    /**
     * Teste le message du prochain tour quand on passe d'un joueur à un autre
     */
    @Test
    void prochainTour() {
        String[] joueurs = {"Joris", "Riyad"};
        partie.demarreNouvellePartie(joueurs);

        // Enregistrer un lancer pour Joris
        assertEquals("Prochain tir : joueur Riyad, tour n° 1, boule n° 1", partie.enregistreLancer(4));
        
        // Enregistrer un lancer pour Riyad
        assertEquals("Prochain tir : joueur Joris, tour n° 2, boule n° 1", partie.enregistreLancer(5));
    }
}
