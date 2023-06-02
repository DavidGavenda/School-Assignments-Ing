import time

# Nacitanie slovnika
# Original slovnik + pridane "a" "i" ako dve jedine slova
# z anglictiny co maju jedno pismeno
slovnik = []
with open('slovnik.txt', "r", encoding='utf-8-sig') as file:
    for line in file:
        slovnik.append(line.rstrip())

# Nacitanie textoveho suboru CHVSTUP
chvstup = []
with open('chvstup.txt', "r", encoding="cp1251") as file:
    for line in file:    
        for word in line.split():      
            chvstup.append(word.lower())


########################    LCS
# https://www.geeksforgeeks.org/longest-common-subsequence-dp-4/
# Longest common subsequence
def lcs(X , Y):
    m = len(X)
    n = len(Y)
    # Vytvorim si M+1 x N+1 prazdny array
    L = [[None]*(n+1) for i in range(m+1)]
    for i in range(m+1):
        for j in range(n+1):
            if i == 0 or j == 0 :
                L[i][j] = 0
            # Spolocne subsequence, pokial su zhodne, zapisem
            elif X[i-1] == Y[j-1]:
                # To co tam mam zvysim o jedno
                L[i][j] = L[i-1][j-1]+1
            else:
                # Zapisem maximalnu hodnotu
                L[i][j] = max(L[i-1][j] , L[i][j-1])
    # Podobne ako predosle zadania
    return L[m][n]


# Hladam slovo, ktore ma najdlhsi LCS so slovom zo slovnika
# Ak najdem rovnake, nehladam dalej 
def findWordLCS(word,slovnik):
    longest = 0
    for Y in slovnik:
        if word[0].isdigit() :
            return word
        value = lcs(Y,word)
        if value>longest:
            found = Y
            longest = value
            if value == len(word):
                return found
    return found

# Zapisujem do suboru, spolu s vypisom do konzoly,
# ktore boli nahradene za co
def opravaLCS():
    file = open("vystup.txt", "w+")
    i = 1
    start = time.time()
    for word in chvstup:
        one = findWordLCS(word, slovnik)
        file.write(one + " ")
        print(i, "/" , len(chvstup), word, "-->", one)
        i += 1
    end = time.time()
    file.close() 
    print("Oprava je hotova -",end - start ,"s")



################ editDist
# https://www.geeksforgeeks.org/edit-distance-dp-5/
def editDist(X, Y):
    m = len(X)
    n = len(Y)
    dp = [[0 for x in range(n + 1)] for x in range(m + 1)]
    # To iste co hore
    for i in range(m + 1):
        for j in range(n + 1):
            # Ak je prvy string prazdny, je treba pridat vsetky z druheho
            if i == 0:
                dp[i][j] = j 
            # Ak je druhy string prazdny, je treba odstranit vsetky z druheho
            elif j == 0:
                dp[i][j] = i 
            # Ak su posledne rovnake, ignoruj a opakuj pre zbytok stringu
            elif X[i-1] == Y[j-1]:
                dp[i][j] = dp[i-1][j-1]
            # Ak su posledne rozdielne, zober minimum zo vsetkych moznosti
            else:
                dp[i][j] = 1 + min(dp[i][j-1],      #Vlozit
                                   dp[i-1][j],      #Odstranit
                                   dp[i-1][j-1])    #Nahradit
    return dp[m][n]

# Pointa je rovnaka ako v LCS
def findWordEditDist(word,slovnik):
    longest = 999
    for Y in slovnik:
        value = editDist(Y, word)
        if word[0].isdigit() :
            return word
        if value<longest:
            found = Y
            longest = value
            if value == 0:
                return found
    return found

def opravaEditDist():
    file = open("vystup.txt", "w+")
    i = 1
    start = time.time()
    for word in chvstup:
        one = findWordEditDist(word, slovnik)
        file.write(one + " ")
        print(i, "/" , len(chvstup), word, "-->", one)
        i += 1
    end = time.time()
    file.close() 
    print("Oprava je hotova -",end - start ,"s")



# https://www.geeksforgeeks.org/longest-common-substring-dp-29/
def LCSubStr(X, Y):
    m = len(X)
    n = len(Y)
    LCSuff = [[0 for k in range(n+1)] for l in range(m+1)]
    result = 0
    for i in range(m + 1):
        for j in range(n + 1):
            if (i == 0 or j == 0):
                LCSuff[i][j] = 0
            elif (X[i-1] == Y[j-1]):
                LCSuff[i][j] = LCSuff[i-1][j-1] + 1
                result = max(result, LCSuff[i][j])
            else:
                LCSuff[i][j] = 0
    return result

def findWordLCSubStr(word,slovnik):
    longest = 0
    for Y in slovnik:
        if word[0].isdigit() :
            return word
        value = LCSubStr(Y,word)
        if value>longest:
            found = Y
            longest = value
            if value == len(word):
                return found
    return found

# Zapisujem do suboru, spolu s vypisom do konzoly,
# ktore boli nahradene za co
def opravaLCSubStr():
    file = open("vystup.txt", "w+")
    i = 1
    start = time.time()
    for word in chvstup:
        one = findWordLCSubStr(word, slovnik)
        file.write(one + " ")
        print(i, "/" , len(chvstup), word, "-->", one)
        i += 1
    end = time.time()
    file.close() 
    print("Oprava je hotova -",end - start ,"s")


# KONTROLA
# Nacitam oba, a po jednom slove ich porovnavam
def kontrola():
    vystup = []
    with open('vystup.txt') as file:
        for line in file:    
            for word in line.split():      
                vystup.append(word.lower())

    vystupCheck = []
    with open('pvstup.txt') as file:
        for line in file:    
            for word in line.split():      
                vystupCheck.append(word.lower())
    good = 0
    for i in range(len(vystupCheck)):
        if vystupCheck[i] == vystup[i]:
            good += 1
        print(vystupCheck[i], "=", vystup[i])
    print("Uspesnost = ", good, "/", len(vystupCheck), '= ', str(round(good/len(vystupCheck)*100,2)),"%")
    return


print("1. oprava (Longest common sequence) \n2. oprava (Longest common substring) \n3. oprava (EditDist) \n4. kontrola")
while 1==1:
    prikaz = input("Zadaj cislo akcie, ktoru chces vykonat: ")
    if(prikaz == "1"):
        opravaLCS()
    elif(prikaz == "2"):
        opravaLCSubStr()
    elif(prikaz == "3"):
        opravaEditDist()
    elif(prikaz == "4"):
        kontrola()
    else:
        print("Nieco zo zoznamu")
