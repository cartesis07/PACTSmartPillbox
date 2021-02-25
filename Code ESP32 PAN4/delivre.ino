void delivre_pilule(int pilule) {
  //navigue dans le table
  int possiton_dans_tableau = find_pilule(pilule);
  if ( possiton_dans_tableau >= 1) {
    //on a bien un medicament
    //envoi position du table +11
    message_to_send(possiton_dans_tableau + 11);
    Serial.println("message to send arduino nano");
    //attendre que le medicament soit bien delivre
    time_delivre_medicament = millis();
    byte sortie_fonction = 1;
    while (sortie_fonction) {
      if ( time_delivre_medicament - millis() > max_time_delivre ) {
        //on a mit trop de temps
        Serial.println("trop de temps pour la reponse");
        //erreur !
        return;
      }
      if ( Wire.read() == delivre) {
        //tout est bon
        Serial.println("bonne delivrance");

        //si oui
        //son bien delivre
        play(avertisement_prise);
        //  capteur_presion();

        //il doit verifier que le medicament a été pris
        demande = 1;
        return;
      }
    }

  } else {
    //on a plus son médicament en stock
    Serial.println("plus de medicament en stock");
    return;
    //si pas alors erreur
  }


  //appel la fonction capteur de pression (check variable si prise) //prise possible
  //si tout est ok
  //son bien pris
  //aussi bug de la prise

}


int find_pilule(int pilule) {
  for (int i = 0; i < compartiment_n ; i++) {
    if (roue[i] == pilule) {
      return i + 1;

    }
  }
  return 0;
}
