INSERT INTO person(uuid, tag, forename, lastname, password, username)
VALUES ('aac05adf-6a65-4206-87fa-d95b3d97e8a1', 0, 'Otto', 'Rohenkohl',
        'ea4607d5ba88b369b5287e15676faa8cc4773d12cd93aa09118a520c28e8a5f7', 'otto.rohenkohl'),
       ('168cfe80-64a0-4a07-b931-a229d6132899', 0, 'Tim', 'Bense',
        '38f3f532579ded3edec2de8a2ee6d4b3392e1f5b0b2d39a47021f07059984f93', 'tim.bense'),
       ('719b0199-e314-42c5-bf35-0438ab85d481', 0, 'Anton', 'Freese',
        'a370809d9e8ea7b90efb91f56eb57cf303086134f661e06d4932d498a228255f', 'anton.freese');

INSERT INTO service(uuid, tag, description, title)
VALUES ('222d033e-7687-464c-972f-fa95b6b65ea2', 0, 'Some unspecified service', 'Hello'),
       ('d93a2148-ee36-43c8-b447-58556663fb90', 0, 'Some different service', 'There');

INSERT INTO permission(uuid, tag, access, person_uuid, service_uuid)
VALUES ('75003075-802b-4630-b8aa-7effe38a190c', 0, 2, 'aac05adf-6a65-4206-87fa-d95b3d97e8a1',
        '222d033e-7687-464c-972f-fa95b6b65ea2'),
       ('481dd68a-2174-40fa-bd15-b1dab9b7f13a', 0, 1, 'aac05adf-6a65-4206-87fa-d95b3d97e8a1',
        'd93a2148-ee36-43c8-b447-58556663fb90'),
       ('c9e2134f-f2d7-4822-bc5c-2f44dea87248', 0, 1, '168cfe80-64a0-4a07-b931-a229d6132899',
        'd93a2148-ee36-43c8-b447-58556663fb90');