import sqlalchemy
from sqlalchemy.ext.declarative import declarative_base

engine = sqlalchemy.create_engine(
    'mysql+mysqlconnector://gituser:gituser1@localhost:3306/mugunga',
    echo=True
)

Base = declarative_base()
class Cantus_Firmus(Base):
    __tablename__ = 'cantus_firmi'

    id = sqlalchemy.Column(sqlalchemy.Integer, primary_key=True)
    mode_id = sqlalchemy.Column(sqlalchemy.SmallInteger)
    melody = sqlalchemy.Column(sqlalchemy.String(length=100))
    timestamp = sqlalchemy.Column(sqlalchemy.DateTime)

    def __repr__(self):
        return "<Cantus_Firmus(mode_id='{0}', melody='{1}')>".format(
            self.mode_id, self.melody
        )

Session = sqlalchemy.orm.sessionmaker()
Session.configure(bind=engine)
session = Session()

newMelody = session.query(Cantus_Firmus).all()
print("new Melody")
print(newMelody)

