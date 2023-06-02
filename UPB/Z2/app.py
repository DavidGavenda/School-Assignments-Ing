from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
from Crypto.Util.Padding import unpad
import os
from datetime import datetime

key = os.urandom(16) #generuje 16 bytovy kluc
cipher = AES.new(key,AES.MODE_CBC)
#vytvara sifru, podla klucu a algoritmu AES
# a mod CBC

print('Zadaj nazov suboru na encrypt: ')
fileName = input()


with open(fileName,mode='rb') as file:  #otvori zadany subor a na cita ho ako bity
    fileContent = file.read()

nowEncrypt = datetime.now() #zaznamenava cas
ciphertext = cipher.encrypt(pad(fileContent,AES.block_size)) #zasifrujem pomocou sifry
#pridavam padding aby to bolo urcite nasobok 128 bitov a pridam AES block size
thenEncrypt = datetime.now()

with open('cipher_file', 'wb') as c_file: #ulozim si sifru s inicializacnym vektorom
    c_file.write(cipher.iv)

with open('encrypted_file', 'wb') as c_file: #ulozim si zasifrovane data "ciphertext"
    c_file.write(ciphertext) 

with open('cipher_file', 'rb') as c_file: #otvaram si ulozeny kluc o velkosti 16 bytov
    iv = c_file.read(16)

nowDecrypt = datetime.now()
cipher = AES.new(key, AES.MODE_CBC,iv) #sifra na desifrovanie, kedze vieme co pouzivame
#vyuzivame nas nacitany IV (inicializacny vektor)
plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
#odstranim padding a desifrujem
thenDecrypt = datetime.now()

text_file = open("output.txt",'w' ,newline='')
#zapiseme nas text do suboru
text_file.write(plaintext.decode())


deltaEncrypt = thenEncrypt - nowEncrypt
deltaDecrypt = thenDecrypt - nowDecrypt
print("Encrypt time: " , deltaEncrypt)
print("Decrypt time: " , deltaDecrypt)

input("Press Enter to continue...")



