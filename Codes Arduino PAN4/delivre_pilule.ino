void delivre_pilule(int compartiment) {
  //reinitialisation
  initialisation_roue();
  delay(2000);
  //on est a la trappe 1 qui est en bas
  //décalage jusqu'au compartiment
  while (position_roue != compartiment) {
    deplacement_un_compartiment(1);
    Serial.println("dans la boucle infine");
  }

  delay(2000);
  //ouvre le servo-moteur
  open_trap_bas();
  delay(1000);
  close_trap_bas();

  //médicament bien délivré
  Wire.write(delivre);
  verife_poid = 1;
  poid_f();
}
