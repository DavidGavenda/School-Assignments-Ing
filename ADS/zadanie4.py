from collections import namedtuple
from operator import itemgetter
from math import sqrt
from collections import defaultdict
import time

#https://www.geeksforgeeks.org/strongly-connected-components/    
class SCC:
    #funkcia na deklaraciu premennych
    def __init__(self):
        self.adj = {}
        self.V = []
 
    def DFSUtil(self, temp, v, visited):
        #nastavim aktualny vrchol na visited
        visited[v] = True
        #pridam vrchol do listu
        temp.append(v)
        #opakujem pre vsetky prilahle vrcholy pre aktualny vrchol
        for i in self.adj[v]:
            if visited[i] == False:
                #aktualizujem list
                temp = self.DFSUtil(temp, i, visited)
        return temp

    #metoda na pridane neorientovanej hrany
    #ak tam este nie je teda
    def addEdge(self, v, w):
        if v in self.adj:
            self.adj[v].append(w)
        else:
            self.adj[v]= [w]
            self.V.append(v)
        if w in self.adj:
            self.adj[w].append(v)
        else:
            self.adj[w] = [v]
            self.V.append(w)

    #metoda na ziskane SCC pre neorientovany graf
    def connectedComponents(self):
        visited = {}
        connectedComponents = []
        for i in self.V:
            visited[i]=False
        for v in self.V:
            if visited[v] == False:
                temp = []
                connectedComponents.append(self.DFSUtil(temp, v, visited))
        return connectedComponents

#https://www.geeksforgeeks.org/union-find/        
class UnionFind:
    def __init__(self,vertices):
        self.V = vertices #pocet vrcholov
        # defaultdict je podclassa slovnika
        # defaultdict nevracia keyError, dava default tam
        # kde nic nie je
        self.graph = defaultdict(list) #slovnik na ukladanie grafu

    #metoda na pridanie hrany do grafu
    def addEdge(self,u,v):
        self.graph[u].append(v)
    
    #metoda na odstranenie hrany z grafu
    def removeEdge(self,u,v):
        self.graph[u].remove(v)

    #metoda na najdenie podmnozinu elementu i
    def find_parent(self, parent,i):
        if parent[i] == -1:
            return i
        if parent[i]!= -1:
             return self.find_parent(parent,parent[i])

    #metoda na vytvorenie "union" dvoch podmnozin
    def union(self,parent,x,y):
        parent[x] = y
 
    #kontrola ci graf obsahuje cyklus
    def isCyclic(self):
        #Alokovanie pamate a vytvorenie vsetkych
        #podmnozing ako jednoprvkove sety
        parent = [-1] * (self.V)
        #Iterovanie cez vsetky hrany grafu
        #Ak su vsetky hrany rovnake = cyklus v grafe
        for i in self.graph:
            for j in self.graph[i]:
                x = self.find_parent(parent, i)
                y = self.find_parent(parent, j)
                if x == y:
                    return True
                self.union(parent,x,y) 
        return False

Node = namedtuple('node', ['value', 'left', 'right'])
#staviam ten k-d strom
# https://en.wikipedia.org/wiki/K-d_tree#Nearest_neighbour_search
def build(points, depth):
    if len(points) == 0: return None    
    points.sort(key = itemgetter(depth % 2))
    middlePoint = (len(points) // 2)
    return Node(
        value = points[middlePoint],
        left = build(
            points=points[:middlePoint],
            depth=depth+1),
        right = build(
            points=points[middlePoint+1:],
            depth=depth+1)
        )

def CalculateDistance(var1,var2):
    return sqrt((var1.x - var2.x)**2 + (var1.y - var2.y)**2)

#Nearest Neighbour
NN = namedtuple('NN', ['point', 'dist'])
#https://johnlekberg.com/blog/2020-04-17-kd-tree.html
def find_nearest_neighbor(*, tree, point):
    #najdi nablizsieho suseda v k-d strome pre dany bod
    bestP = None
    bestV = None
    #rekurzivne vyhladavanie najblizsieho suseda
    #zacina v koreni a ide dole rekurzivne, zlava do prava
    #verzia binarneho stromu
    def search(*, tree, depth):
        nonlocal bestP
        nonlocal bestV
        if tree is None:
            return
        #hned ako dojde na list, skontroluje vzdialenost a ulozi ak je najlepsia
        distance = CalculateDistance(tree.value, point)
        #pokial este nemam best alebo je lepsi nez aktualny, ulozim
        if bestV is None or distance < bestV:
            bestP = tree.value
            bestV = distance
        #rozdelim na hyperplanes, kedze su allignute
        #porovnavanie ci rozdelovaci bod(aktualny)
        #je lepsi nez najlepsi ktory mam
        axis = depth % 2
        diff = point[axis] - tree.value[axis]
        #porovnavam aby som vedel na ktorej strane
        #sa moze nachadzat mensia cesta
        #druhu viac menej ignorujem
        if diff <= 0:
            close, away = tree.left, tree.right
        else:
            close, away = tree.right, tree.left
        
        search(tree=close, depth=depth+1)
        if diff < bestV:
            search(tree=away, depth=depth+1)
    #ked sa dostanem na koren, koncim
    search(tree=tree, depth=0)
    return NN(bestP, bestV)

startTime = time.time()
fileName = 'input.txt'
g = SCC()
Point = namedtuple('point', ['x', 'y'])
with open(fileName) as f:
    for i, line in enumerate(f):
        line = line.rstrip().split(' ')
        point1 = line[0][1:-1].split(',')
        point2 = line[1][1:-1].split(',')
        #beriem prve a odstranim [ ] teda prvy a posledny znak
        #nasledne splitnem podla ciarky a mam prvu a druhu premennu
        g.addEdge(Point(x = int(point1[0]),y = int(point1[1])), Point(x = int(point2[0]),y = int(point2[1])))
        #pridam si hrany do grafu
connectedComponents = g.connectedComponents()
print("Pocet SCC =", len(connectedComponents))


Line = namedtuple('line', ['x', 'y', 'dist'])
trees = [build(x,0) for x in connectedComponents]
arr = [[Line(0, 0, 2147483647) for i in range(len(connectedComponents))] for j in range(len(connectedComponents))]

for i, group in enumerate(connectedComponents):
    for j, tree in enumerate(trees[i+1:], i+1):
        point1 = 0
        point2 = 0
        foundDist = 2147483647
        for point in group:
            best = find_nearest_neighbor(tree= tree, point = point)
            if best.dist < foundDist:
                point1 = point
                point2 = best.point
                foundDist = best.dist
        arr[i][j] = Line(point1, point2, foundDist)
    
finalResult = 0
g = UnionFind(len(connectedComponents))
exportFile = 'out/' + fileName
with open(exportFile, 'w+')as f:
    edges = 0
    #spoje medzi vsetkymi SCC
    while(edges!=len(connectedComponents)-1):
        point = arr[0][0]
        newI, newJ = 0, 0
        for i, line in enumerate(arr):
            for j, x in enumerate(line[i+1:], i+1):
                if x[2]<point[2]:
                    point = x
                    newI, newJ = i, j
        g.addEdge(newI,newJ)
        #ak je to cyklus, odstranim hranu
        if g.isCyclic():
            g.removeEdge(newI, newJ)
        else:    
            finalResult += point[2]
            edges += 1
            #zapisujem body
            f.write(f"[{point[0].x},{point[0].y}] [{point[1].x},{point[1].y}] \n")
        arr[newI][newJ] = arr[0][0]
endTime = time.time()
print('Suma najdenych pridanych ciest =', round(finalResult,6))
print('Cas zbehnutia =',round(endTime-startTime,2),'sekund =',round((endTime-startTime)/60,2),'minut')