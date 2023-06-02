from Crypto.Cipher import AES, PKCS1_OAEP
from Crypto.Random import get_random_bytes
from Crypto.PublicKey import RSA
from base64 import b64encode
from base64 import b64decode
import json

def decrypt():
    try:
        file_to_decrypt = open("encrypted.json", "r")
        json_encrypted = file_to_decrypt.read()
        b64 = json.loads(json_encrypted)
        json_k = [ 'nonce', 'header', 'ciphertext', 'tag' ]
        jv = {k:b64decode(b64[k]) for k in json_k}

        private_rsa_key = RSA.import_key(open("private.pem").read())
        cipher_rsa = PKCS1_OAEP.new(private_rsa_key)
        key = cipher_rsa.decrypt(bytes(jv['header']))

        cipher = AES.new(key, AES.MODE_GCM, nonce=jv['nonce'])
        cipher.update(jv['header'])
        data = cipher.decrypt_and_verify(jv['ciphertext'], jv['tag'])
        
        file_to_decrypt = open("decrypted.bin", "wb")
        file_to_decrypt.write(data)
        file_to_decrypt.close()

        print("----------Decryption done----------")
    except (ValueError, KeyError):
            print("Error")



def encrypt(file_name):
    symmetric_key = get_random_bytes(16)

    public_rsa_key = RSA.import_key(open("public.pem").read())
    cipher_rsa = PKCS1_OAEP.new(public_rsa_key)
    encrypted_key = cipher_rsa.encrypt(symmetric_key)

    header = encrypted_key
    file_to_encrypt = open(file_name, 'rb')
    data = file_to_encrypt.read()

    cipher = AES.new(symmetric_key, AES.MODE_GCM)
    cipher.update(header)
    ciphertext, tag = cipher.encrypt_and_digest(data)

    json_k = [ 'nonce', 'header', 'ciphertext', 'tag' ]
    json_v = [ b64encode(x).decode('utf-8') for x in [cipher.nonce, header, ciphertext, tag ]]

    result = json.dumps(dict(zip(json_k, json_v)))

    file_to_encrypt = open("encrypted.json", "w")
    file_to_encrypt.write(result)
    file_to_encrypt.close()

    print("----------Encryption done----------")

def generate_key():
    rsa_key = RSA.generate(2048)
    private_rsa_key = rsa_key.export_key()
    public_rsa_key = rsa_key.publickey().export_key()

    file_private_key = open("private.pem", "wb")
    file_private_key.write(private_rsa_key)
    file_private_key.close()

    file_public_key = open("public.pem", "wb")
    file_public_key.write(public_rsa_key)
    file_public_key.close()

    print("----------Key generated----------")


while(1):
    print("generate - Generate key\nencrypt - Encrypt file\ndecrypt - Decrypt file\nquit")
    choice = input("Chose wisely: ")

    if choice == "generate":
        generate_key()
    elif choice == "encrypt":
        file_name = input("File name: ")
        encrypt(file_name)
    elif choice == "decrypt":
        decrypt()
    elif choice == "quit":
        break
    else:
        print("Wrong choice")

