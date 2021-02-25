void open_trap_haut() {
  servo1_haut.write(position_ferme);
}

void close_trap_haut() {
  servo1_haut.write(position_ouvert);
}

void open_trap_bas() {
  servo1_bas.write(position_ferme);
}

void close_trap_bas() {
  servo1_bas.write(position_ouvert);
}
