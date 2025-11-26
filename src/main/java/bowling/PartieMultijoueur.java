package bowling;


import java.util.HashMap;
import java.util.Map;

/**
 * Classe qui implémente l'interface IPartieMultiJoueurs pour une partie de bowling
 * avec plusieurs joueurs.
 */
public class PartieMultijoueur implements IPartieMultiJoueurs {

    private Map<String, PartieMonoJoueur> joueurs;  // Map des joueurs et leurs parties respectives
    private String[] nomsDesJoueurs;                // Liste des joueurs
    private int joueurCourantIndex;                  // Index du joueur dont c'est le tour
    private boolean partieDemarree;                  // Indique si la partie a commencé

    /**
     * Constructeur par défaut
     */
    public PartieMultijoueur() {
        this.joueurs = new HashMap<>();
        this.partieDemarree = false;
    }

    @Override
    public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
        if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
            throw new IllegalArgumentException("Le tableau des joueurs ne peut pas être vide ou nul.");
        }

        this.nomsDesJoueurs = nomsDesJoueurs;
        this.joueurCourantIndex = 0;
        this.partieDemarree = true;

        // Crée une nouvelle partie pour chaque joueur
        for (String nom : nomsDesJoueurs) {
            joueurs.put(nom, new PartieMonoJoueur());
        }

        return prochainTour();  // Retourne la description du prochain tour
    }

    @Override
    public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
        if (!partieDemarree) {
            throw new IllegalStateException("La partie n'a pas encore démarré.");
        }

        String joueurCourant = nomsDesJoueurs[joueurCourantIndex];
        PartieMonoJoueur partie = joueurs.get(joueurCourant);

        // Enregistre le lancer pour le joueur courant
        boolean continuer = partie.enregistreLancer(nombreDeQuillesAbattues);

        if (!continuer) {
            // Passe au joueur suivant si le tour est terminé
            joueurCourantIndex = (joueurCourantIndex + 1) % nomsDesJoueurs.length;
        }

        if (partie.estTerminee()) {
            return "Partie terminée";
        }

        return prochainTour();  // Retourne la description du prochain tir
    }

    @Override
    public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
        if (!joueurs.containsKey(nomDuJoueur)) {
            throw new IllegalArgumentException("Le joueur " + nomDuJoueur + " n'existe pas dans cette partie.");
        }

        return joueurs.get(nomDuJoueur).score();
    }

    /**
     * Retourne la chaîne décrivant le prochain tir.
     * @return une chaîne de caractères indiquant le prochain tir à effectuer
     */
    private String prochainTour() {
        String joueurCourant = nomsDesJoueurs[joueurCourantIndex];
        PartieMonoJoueur partie = joueurs.get(joueurCourant);
        int numeroTour = partie.numeroTourCourant();
        int numeroLancer = partie.numeroProchainLancer();

        return String.format("Prochain tir : joueur %s, tour n° %d, boule n° %d", joueurCourant, numeroTour, numeroLancer);
    }
}
