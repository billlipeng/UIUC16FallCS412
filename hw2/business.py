def readinfile():
    cnt = 0
    with open("data.business", "rw+") as fp:
        for line in fp:
            if "Illinois" in line:
                if "3" in line:
                    if "moderate" in line:
                        cnt += 1
                        print (str(cnt) + "  " + line)

def readinfile2():
    cnt = 0
    with open("data.business", "rw+") as fp:
        for line in fp:
            if "Chicago" in line:
                if "food" in line:
                    cnt += 1
                    print (str(cnt) + "  " + line)

readinfile2()
