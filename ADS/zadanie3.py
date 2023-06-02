# http://code.activestate.com/recipes/578507-strongly-connected-components-of-a-directed-graph/
def strongly_connected_components_iterative(vertices, edges):
    identified = set()
    stack,boundaries,tmp__ = ([] for i in range(3)) #3 listy v jednom riadku
    index = {}
    for v in vertices:
        if v not in index:
            to_do = [('VISIT', v)]
            while to_do:
                operation_type, v = to_do.pop()
                if operation_type == 'VISIT':
                    index[v] = len(stack)
                    stack.append(v)
                    boundaries.append(index[v])
                    to_do.append(('POSTVISIT', v))
                    to_do.extend(
                        reversed([('VISITEDGE', w) for w in edges[v]]))
                elif operation_type == 'VISITEDGE':
                    if v not in index:
                        to_do.append(('VISIT', v))
                    elif v not in identified:
                        while index[v] < boundaries[-1]:
                            boundaries.pop()
                else:
                    if boundaries[-1] == index[v]:
                        boundaries.pop()
                        SCC = set(stack[index[v]:])
                        del stack[index[v]:]
                        identified.update(SCC)
                        tmp__.append(SCC)
    return tmp__

# skontroluje ci sa v jednotlivych silnych komponentoch nachadza 1 -1 alebo nie
# ak nie tak vrati True, v opacnom pripade False
def isSatisfiable(SCC):
    satisfiable = True
    for one in SCC:
        for itemOne in one:
            if ((-itemOne) in one):
                satisfiable = False
                break
    return satisfiable

# Urcovanie pravda/nepravda pre vrcholy
def DistributionOfVariables(SCC,vertices):
    VerticesMap = dict.fromkeys(vertices, None)
    for one in SCC:
        for itemOne in one:
            if (VerticesMap.get(itemOne) == None): # ak nie je nic nastavene pokracuj
                if (itemOne >= 1):#ak je vacsie ako 1, potom je kladne, nastav PRAVDA
                    VerticesMap.update({itemOne: 'PRAVDA'})
                else: # inaksie je zaporne a, nastav NEPRAVDA
                    if (VerticesMap.get(-itemOne) == None):
                        VerticesMap.update({-itemOne: 'NEPRAVDA'})
    return VerticesMap

edges = {}
SAT_form = []
SAT_doc = []

# Nacitam subor po riadkoch
vstup = []
clausules = []
with open("vstup.txt") as input:
    for line in input:
        vstup.append(line.strip().split())

# Dam si do pola vsetky riadky okrem prveho
for clausule in vstup[1:]: #preskakujem prvy, kde su tie pocty
    clausules.append(clausule[:-1]) #preskakujem posledny, kde je len nula

# clausules je v KNF formate
# Zoberiem si hodnoty z prveho riadku
nbvar = int(vstup[0][0])
nbclauses = int(vstup[0][1])
if len(clausules) == nbclauses: #pocet klauzul musi sediet so zadanym
    vertices = []
    for i in range(1,nbvar + 1): 
        vertices.append(i)

    for i in range(1,nbvar + 1):
        vertices.append(-i)

    # Z pola vytvaram graf, na zaklade a∨b = ¬a⇒b∧¬b⇒a
    for i in range(len(clausules)):              #cyklus cez formulu
        for j in range(len(clausules[i])):       #cyklus cez klauzulu
            if len(clausules[i]) > 1:
                if j == 0:
                    if (len(clausules[i][j])) < 2:
                        SAT_doc.append(int(clausules[i][j]) * - 1)
                        SAT_doc.append(int(clausules[i][j + 1]))
                    else:
                        SAT_doc.append(int(clausules[i][j][1]))
                        SAT_doc.append(int(clausules[i][j + 1]))
                elif j == 1:
                    if (len(clausules[i][j])) < 2:
                        SAT_doc.append(int(clausules[i][j]) * - 1)
                        SAT_doc.append(int(clausules[i][j - 1]))
                    else:
                        SAT_doc.append(int(clausules[i][j][1]))
                        SAT_doc.append(int(clausules[i][j - 1]))
                SAT_form.append(SAT_doc)
                SAT_doc = []
            elif len(clausules[i]) == 1:
                if (len(clausules[i][j])) < 2:
                    SAT_doc.append(int(clausules[i][j]) * - 1)
                    SAT_doc.append(int(clausules[i][j]))
                else:
                    SAT_doc.append(int(clausules[i][j][1]))
                    SAT_doc.append(int(clausules[i][j]))
        if len(SAT_doc) > 0:
            SAT_form.append(SAT_doc)
            SAT_doc = []

    # kvoli SCC (zmena tvaru)
    for i in range(len(vertices)):
        edge = []
        for j in range(len(SAT_form)):
            if str(vertices[i]) == str(SAT_form[j][0]):
                edge.append(int(SAT_form[j][1]))
        edges[vertices[i]] = edge

    SCC = strongly_connected_components_iterative(vertices, edges)

    if isSatisfiable(SCC): #ak vrati True, teda je splnitelna
        TrueValues = DistributionOfVariables(SCC, vertices)
        print("SPLNITELNA")
        for item in TrueValues:
            if (item > 0):
                print(str(item)+':', TrueValues.get(item))
    else: # v opacnom pripade splnitelna nie je 
        print("NESPLNITELNA")
else: 
    print("Zly subor")
