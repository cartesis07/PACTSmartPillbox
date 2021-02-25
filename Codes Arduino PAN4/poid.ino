void poid_f() {

  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  poid = scale.get_units();
  Serial.println("moi : ");
  Serial.println(poid - set_up_poid);
  Serial.println();
  poid_enregistre = poid - set_up_poid ;

}

void poid_test() {
  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  poid = scale.get_units();
  if (poid - set_up_poid >= poid_enregistre + 0.1 || poid - set_up_poid <= poid_enregistre - 0.1) {
    //le médicament a été pris
    Serial.println("médicament pris");
    Wire.write(prise_medicament);
    verife_poid = 0;
  }
}
