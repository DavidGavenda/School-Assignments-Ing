#https://www.geeksforgeeks.org/insert-a-node-in-binary-search-tree-iteratively/
class Node:
    def __init__(self, data):

        self.key = data
        self.left = None
        self.right = None

    def insert(root, data):
        newnode = Node(data)
        x = root
        y = None

        while (x != None):
            y = x
            if (data < x.key):
                x = x.left
            else:
                x = x.right

        if (y == None):
            y = newnode

        elif (data < y.key):
            y.left = newnode

        else:
            y.right = newnode

        return y

    #tu som teda len zmenil nazov
    def pocet_porovnani(self, data):
        counter = 0
        while self:
            if data < self.key:
                self = self.left
                counter += 1
            elif data > self.key:
                self = self.right
                counter += 1
            else:
                string = f"Pocet porovnani na najdenie slova '{data}' = {counter+1}"
                print(string)
                return 1
        string = f"Slovo '{data}' sa v strome nenachadza"
        print(string)
        return 0

############################
#zdroj je kniha

def OBST(p, q, n):
    e = [[0 for x in range(n + 1)] for y in range(n + 1)]  
    w = [[0 for x in range(n + 1)] for y in range(n + 1)]  
    root = [[0 for x in range(n + 1)] for y in range(n + 1)]  

    for i in range(0, n + 1):
        e[i][i] = q[i]
        w[i][i] = q[i]

    for l in range(1, n + 1):
        for i in range(0, n - l + 1):
            j = i + l
            e[i][j] = 2147483647 #sys.maxsize()
            w[i][j] = w[i][j - 1] + p[j] + q[j]

            for r in range(i, j + 1):
                t = e[i][r - 1] + e[r][j] + w[i][j]
                if t < e[i][j]:
                    e[i][j] = t
                    root[i + 1][j] = r
    return e, root

########################
#https://www.youtube.com/watch?v=CTUTPSXyBO8&t=210s&ab_channel=ChrisBourke
def createTree(keys, root_matrix):
    n = len(keys)
    root_node = root_matrix[1][n]
    root_node_value = keys[root_node - 1][1]
    node = Node(root_node_value)

    arr = [[node, 1, n]]
    while arr:
        node, i, n = arr.pop()
        next_root = root_matrix[i][n]
        if next_root < 0:
            break
        else:
            if i < next_root:
                root_node = root_matrix[i][next_root - 1]
                root_node_value = keys[root_node - 1][1]
                node.insert(root_node_value)
                arr.append([node, i, next_root - 1])
            if next_root < n:
                root_node = root_matrix[next_root + 1][n]
                root_node_value = keys[root_node - 1][1]
                node.insert(root_node_value)
                arr.append([node, next_root + 1, n])
    return node

######################
#https://github.com/ritu-thombre99/Optimal-Binary-Search-Tree-for-Dictionary-Implementation/blob/main/obst.ipynb
def height(node): 
    if node is None: 
        return 0 
    else : 
        # Vypocitaj vysku kazdeho podstromu
        lheight = height(node.left) 
        rheight = height(node.right) 
  
        # Pouzi vacsi podstrom
        if lheight > rheight : 
            return lheight+1
        else: 
            return rheight+1
        
def printLevelOrder(root): 
    h = height(root) 
    for i in range(1, h+1): 
        print('-------------------------------------------------------------------------------------------------------------------------------')
        x=[]
        print("Level:",i)
        printGivenLevel(root, i,x) 
        print(x)
    print('-------------------------------------------------------------------------------------------------------------------------------')

def printGivenLevel(root , level,x): 
    if root is None: 
        return
    if level == 1: 
        x.append(root.data) 
    elif level > 1 : 
        printGivenLevel(root.left , level-1,x) 
        printGivenLevel(root.right , level-1,x)

###########################



# Nacitavam dictionary a usporiadam ho cely
entireDictionary = []
with open("dictionary.txt","r") as file:
    for line in file:
        one = line.split()
        entireDictionary.append(one)

entireDictionary.sort(key=lambda x: x[1]) #usporiadam lexikograficky

wantedKeys = []
otherKeys = []
totalFreq = 0
P=[0] #na prvom mieste je 0
Q=[]
qFreq = 0

#spocitam na zistenie frekvencie
for one in entireDictionary:
    totalFreq += int(one[0])

# ziskavam len tie s frekvenciou 50 000 +
for one in entireDictionary:
    freq = int(one[0]) #prva je frekvencia, druhe je slovo
    if freq > 50000: #ak je frekvencia viac ako 50 000 pridam cely riadok
        wantedKeys.append(one)
        P.append(freq/totalFreq)
        Q.append(qFreq/totalFreq)
        qFreq = 0
    else:
        otherKeys.append(one)
        qFreq += freq

Q.append(qFreq/totalFreq)

e, root = OBST(P,Q,len(wantedKeys))
tree = createTree(wantedKeys, root)

#Ak chcem strom vykreslit
# printLevelOrder(tree)

print("------------------------------------")
print("OBST cena =",e[0][len(wantedKeys)])
print("Ak chces skoncit napis 'i am done'")
print("------------------------------------")

while 1==1:
    searchedWord = input("Zadaj hladane slovo: ")
    if(searchedWord == "i am done"):
        break
    else:
        tree.pocet_porovnani(searchedWord)