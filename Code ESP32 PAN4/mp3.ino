void play(unsigned char Track)
{
  unsigned char play[6] = {0xAA, 0x07, 0x02, 0x00, Track, Track + 0xB3};
  swSer.write(play, 6);
}
void volume( unsigned char vol)
{
  unsigned char volume[5] = {0xAA, 0x13, 0x01, vol, vol + 0xBE};
  swSer.write(volume, 5);
}
