void deplacement_un_compartiment(byte sense) {
  Serial.println("go rotation");
  if (sense) {
    myStepper.step(v_compartiment);
    //enregistre la position
    position_roue = position_roue + 1;
  } else {
    myStepper.step(-v_compartiment);
    position_roue = position_roue - 1;
  }

}
